package com.capstone.subway.station;

import com.capstone.subway.common.exception.NotFoundException;
import com.capstone.subway.station.dto.StationResponse;
import com.capstone.subway.station.dto.StationSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public List<StationResponse> findAll() {
        return stationRepository.findAll().stream().map(StationResponse::from).toList();
    }

    public StationResponse findById(Long stationId) {
        Station station = getStationOrThrow(stationId);
        return StationResponse.from(station);
    }

    public StationSearchResponse search(String keyword) {
        List<StationResponse> stations = stationRepository.findByStationNameContainingIgnoreCaseOrderByStationNameAsc(keyword).stream()
                .map(StationResponse::from)
                .toList();
        return StationSearchResponse.builder()
                .keyword(keyword)
                .count(stations.size())
                .stations(stations)
                .build();
    }

    public Station getStationOrThrow(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException("Station not found: " + stationId));
    }
}
