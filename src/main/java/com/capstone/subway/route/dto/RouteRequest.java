package com.capstone.subway.route.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RouteRequest {

    @NotNull
    private Long fromStationId;

    @NotNull
    private Long toStationId;
}
