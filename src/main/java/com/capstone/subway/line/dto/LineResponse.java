package com.capstone.subway.line.dto;

import com.capstone.subway.line.Line;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LineResponse {

    private final Long id;
    private final String lineName;
    private final String lineColor;

    public static LineResponse from(Line line) {
        return LineResponse.builder()
                .id(line.getId())
                .lineName(line.getLineName())
                .lineColor(line.getLineColor())
                .build();
    }
}
