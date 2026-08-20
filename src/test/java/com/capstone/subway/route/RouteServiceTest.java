package com.capstone.subway.route;

import com.capstone.subway.line.Line;
import com.capstone.subway.section.StationSection;
import com.capstone.subway.station.Station;
import com.capstone.subway.transfer.TransferInfo;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RouteServiceTest {

    private Station station(long id, String name, Line line) {
        return Station.builder().id(id).stationName(name).line(line).build();
    }

    @Test
    void findsShortestPathAlongSingleLine() {
        Line line1 = Line.builder().id(1L).lineName("Line 1").build();
        Station a = station(1, "A", line1);
        Station b = station(2, "B", line1);
        Station c = station(3, "C", line1);

        List<StationSection> sections = List.of(
                StationSection.builder().id(1L).fromStation(a).toStation(b).durationSeconds(120).distanceKm(1.0).build(),
                StationSection.builder().id(2L).fromStation(b).toStation(c).durationSeconds(150).distanceKm(1.5).build()
        );

        RouteResult result = RouteService.computeShortestRoute(a, c, sections, List.of());

        assertThat(result.getPath()).extracting(Station::getId).containsExactly(1L, 2L, 3L);
        assertThat(result.getTotalDurationSeconds()).isEqualTo(270);
        assertThat(result.getTotalDistanceKm()).isEqualTo(2.5);
        assertThat(result.getTransferCount()).isZero();
    }

    @Test
    void routeIsTraversableInReverseDirectionTooSinceSectionsAreUndirected() {
        Line line1 = Line.builder().id(1L).lineName("Line 1").build();
        Station a = station(1, "A", line1);
        Station b = station(2, "B", line1);

        List<StationSection> sections = List.of(
                StationSection.builder().id(1L).fromStation(a).toStation(b).durationSeconds(100).distanceKm(1.0).build()
        );

        RouteResult result = RouteService.computeShortestRoute(b, a, sections, List.of());

        assertThat(result.getPath()).extracting(Station::getId).containsExactly(2L, 1L);
        assertThat(result.getTotalDurationSeconds()).isEqualTo(100);
    }

    @Test
    void appliesTransferTimeAndCountsTransfersWhenSwitchingLines() {
        Line line1 = Line.builder().id(1L).lineName("Line 1").build();
        Line line2 = Line.builder().id(2L).lineName("Line 2").build();
        Station a = station(1, "A", line1);
        Station transferHub1 = station(2, "Hub-L1", line1);
        Station transferHub2 = station(3, "Hub-L2", line2);
        Station d = station(4, "D", line2);

        List<StationSection> sections = List.of(
                StationSection.builder().id(1L).fromStation(a).toStation(transferHub1).durationSeconds(100).distanceKm(1.0).build(),
                StationSection.builder().id(2L).fromStation(transferHub2).toStation(d).durationSeconds(100).distanceKm(1.0).build()
        );
        List<TransferInfo> transfers = List.of(
                TransferInfo.builder().id(1L).fromStation(transferHub1).toStation(transferHub2).transferTimeSec(180).build()
        );

        RouteResult result = RouteService.computeShortestRoute(a, d, sections, transfers);

        assertThat(result.getPath()).extracting(Station::getId).containsExactly(1L, 2L, 3L, 4L);
        assertThat(result.getTotalDurationSeconds()).isEqualTo(100 + 180 + 100);
        assertThat(result.getTotalDistanceKm()).isEqualTo(2.0);
        assertThat(result.getTransferCount()).isEqualTo(1);
    }

    @Test
    void prefersFasterPathOverShorterHopCountWhenTransferIsCostly() {
        Line line1 = Line.builder().id(1L).lineName("Line 1").build();
        Station a = station(1, "A", line1);
        Station b = station(2, "B", line1);
        Station c = station(3, "C", line1);
        Station d = station(4, "D", line1);

        // Direct but slow path A->D via a costly transfer edge, vs a longer-hop but faster path A->B->C->D.
        List<StationSection> sections = List.of(
                StationSection.builder().id(1L).fromStation(a).toStation(b).durationSeconds(60).distanceKm(1).build(),
                StationSection.builder().id(2L).fromStation(b).toStation(c).durationSeconds(60).distanceKm(1).build(),
                StationSection.builder().id(3L).fromStation(c).toStation(d).durationSeconds(60).distanceKm(1).build()
        );
        List<TransferInfo> transfers = List.of(
                TransferInfo.builder().id(1L).fromStation(a).toStation(d).transferTimeSec(1000).build()
        );

        RouteResult result = RouteService.computeShortestRoute(a, d, sections, transfers);

        assertThat(result.getPath()).extracting(Station::getId).containsExactly(1L, 2L, 3L, 4L);
        assertThat(result.getTotalDurationSeconds()).isEqualTo(180);
        assertThat(result.getTransferCount()).isZero();
    }
}
