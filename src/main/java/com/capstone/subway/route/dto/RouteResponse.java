package com.capstone.subway.route.dto;

import com.capstone.subway.station.dto.StationResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RouteResponse {

    private final List<StationResponse> path;
    private final double totalDistanceKm;
    private final long totalDurationSeconds;
    private final int transferCount;
    private final int fare;
}
