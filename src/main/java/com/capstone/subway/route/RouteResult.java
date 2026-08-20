package com.capstone.subway.route;

import com.capstone.subway.station.Station;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RouteResult {

    private final List<Station> path;
    private final double totalDistanceKm;
    private final long totalDurationSeconds;
    private final int transferCount;
}
