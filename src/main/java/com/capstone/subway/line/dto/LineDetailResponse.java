package com.capstone.subway.line.dto;

import com.capstone.subway.line.Line;
import com.capstone.subway.station.Station;
import com.capstone.subway.station.dto.StationResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LineDetailResponse {

    private final Long id;
    private final String lineName;
    private final String lineColor;
    private final List<StationResponse> stations;

    public static LineDetailResponse of(Line line, List<Station> stations) {
        return LineDetailResponse.builder()
                .id(line.getId())
                .lineName(line.getLineName())
                .lineColor(line.getLineColor())
                .stations(stations.stream().map(StationResponse::from).toList())
                .build();
    }
}
