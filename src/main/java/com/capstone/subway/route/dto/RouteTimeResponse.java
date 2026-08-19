package com.capstone.subway.route.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RouteTimeResponse {

    private final long totalDurationSeconds;
    private final int transferCount;
}
