package com.capstone.subway.route;

import com.capstone.subway.common.exception.BadRequestException;
import com.capstone.subway.common.exception.NotFoundException;
import com.capstone.subway.fare.FareCalculator;
import com.capstone.subway.section.StationSection;
import com.capstone.subway.section.StationSectionRepository;
import com.capstone.subway.station.Station;
import com.capstone.subway.station.StationService;
import com.capstone.subway.transfer.TransferInfo;
import com.capstone.subway.transfer.TransferInfoRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Computes the shortest route between two stations using Dijkstra's algorithm.
 *
 * Graph construction note: StationSection rows are stored as directed edges
 * (from_station -> to_station) but are treated as an UNDIRECTED graph here —
 * each section contributes an edge in both directions, since Daegu subway
 * lines can be traveled either way along the same track. TransferInfo rows
 * are also added as bidirectional edges whose weight is the transfer time;
 * traversing such an edge increments the transfer count for the path.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RouteService {

    private final StationService stationService;
    private final StationSectionRepository stationSectionRepository;
    private final TransferInfoRepository transferInfoRepository;
    private final FareCalculator fareCalculator;

    public RouteResult findShortestRoute(Long fromStationId, Long toStationId) {
        if (Objects.equals(fromStationId, toStationId)) {
            throw new BadRequestException("fromStationId and toStationId must differ");
        }
        Station from = stationService.getStationOrThrow(fromStationId);
        Station to = stationService.getStationOrThrow(toStationId);

        List<StationSection> sections = stationSectionRepository.findAll();
        List<TransferInfo> transfers = transferInfoRepository.findAll();

        RouteResult result = computeShortestRoute(from, to, sections, transfers);
        if (result == null) {
            throw new NotFoundException("No route found between stations " + fromStationId + " and " + toStationId);
        }
        // Force-initialize each station's lazy `line` association while the
        // transaction (and its persistence-context session) is still open, so
        // that controllers can safely map to StationResponse afterwards even
        // under open-in-view: false (e.g. the postgres profile).
        result.getPath().forEach(station -> Hibernate.initialize(station.getLine()));
        return result;
    }

    public int fareFor(double totalDistanceKm) {
        return fareCalculator.calculateFare(totalDistanceKm);
    }

    /**
     * Pure Dijkstra computation over an explicit graph fixture — no Spring/DB dependency,
     * usable directly from unit tests.
     */
    public static RouteResult computeShortestRoute(Station from, Station to,
                                                     List<StationSection> sections,
                                                     List<TransferInfo> transfers) {
        Map<Long, List<Edge>> graph = buildGraph(sections, transfers);

        Map<Long, Long> bestWeight = new HashMap<>();
        Map<Long, Station> prevStation = new HashMap<>();
        Map<Long, Edge> prevEdge = new HashMap<>();

        PriorityQueue<long[]> queue = new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));
        bestWeight.put(from.getId(), 0L);
        queue.add(new long[]{from.getId(), 0L});

        Set<Long> visited = new HashSet<>();

        while (!queue.isEmpty()) {
            long[] current = queue.poll();
            long currentId = current[0];
            if (visited.contains(currentId)) {
                continue;
            }
            visited.add(currentId);
            if (currentId == to.getId()) {
                break;
            }
            List<Edge> edges = graph.getOrDefault(currentId, List.of());
            for (Edge edge : edges) {
                long candidate = bestWeight.get(currentId) + edge.weightSeconds;
                Long existing = bestWeight.get(edge.to.getId());
                if (existing == null || candidate < existing) {
                    bestWeight.put(edge.to.getId(), candidate);
                    prevStation.put(edge.to.getId(), edge.from);
                    prevEdge.put(edge.to.getId(), edge);
                    queue.add(new long[]{edge.to.getId(), candidate});
                }
            }
        }

        if (!bestWeight.containsKey(to.getId())) {
            return null;
        }

        LinkedList<Station> path = new LinkedList<>();
        double totalDistance = 0;
        int transferCount = 0;
        Station cursor = to;
        path.addFirst(cursor);
        while (!cursor.getId().equals(from.getId())) {
            Edge edge = prevEdge.get(cursor.getId());
            totalDistance += edge.distanceKm;
            if (edge.isTransfer) {
                transferCount++;
            }
            cursor = prevStation.get(cursor.getId());
            path.addFirst(cursor);
        }

        return RouteResult.builder()
                .path(path)
                .totalDistanceKm(totalDistance)
                .totalDurationSeconds(bestWeight.get(to.getId()))
                .transferCount(transferCount)
                .build();
    }

    private static Map<Long, List<Edge>> buildGraph(List<StationSection> sections, List<TransferInfo> transfers) {
        Map<Long, List<Edge>> graph = new HashMap<>();
        for (StationSection section : sections) {
            addEdge(graph, section.getFromStation(), section.getToStation(), section.getDurationSeconds(), section.getDistanceKm(), false);
            addEdge(graph, section.getToStation(), section.getFromStation(), section.getDurationSeconds(), section.getDistanceKm(), false);
        }
        for (TransferInfo transfer : transfers) {
            addEdge(graph, transfer.getFromStation(), transfer.getToStation(), transfer.getTransferTimeSec(), 0.0, true);
            addEdge(graph, transfer.getToStation(), transfer.getFromStation(), transfer.getTransferTimeSec(), 0.0, true);
        }
        return graph;
    }

    private static void addEdge(Map<Long, List<Edge>> graph, Station from, Station to, int weightSeconds, double distanceKm, boolean isTransfer) {
        graph.computeIfAbsent(from.getId(), k -> new ArrayList<>())
                .add(new Edge(from, to, weightSeconds, distanceKm, isTransfer));
    }

    private record Edge(Station from, Station to, int weightSeconds, double distanceKm, boolean isTransfer) {
    }
}
