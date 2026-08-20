package com.capstone.subway.route.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RouteFareResponse {

    private final double totalDistanceKm;
    private final int fare;
}
