package com.capstone.subway.station.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StationSearchResponse {

    private final String keyword;
    private final int count;
    private final List<StationResponse> stations;
}
