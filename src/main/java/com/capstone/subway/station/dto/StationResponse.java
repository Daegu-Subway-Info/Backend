package com.capstone.subway.station.dto;

import com.capstone.subway.station.Station;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StationResponse {

    private final Long id;
    private final String stationName;
    private final String stationCode;
    private final Long lineId;
    private final String lineName;
    private final Integer sequenceNo;
    private final Double latitude;
    private final Double longitude;

    public static StationResponse from(Station station) {
        return StationResponse.builder()
                .id(station.getId())
                .stationName(station.getStationName())
                .stationCode(station.getStationCode())
                .lineId(station.getLine() != null ? station.getLine().getId() : null)
                .lineName(station.getLine() != null ? station.getLine().getLineName() : null)
                .sequenceNo(station.getSequenceNo())
                .latitude(station.getLatitude())
                .longitude(station.getLongitude())
                .build();
    }
}
