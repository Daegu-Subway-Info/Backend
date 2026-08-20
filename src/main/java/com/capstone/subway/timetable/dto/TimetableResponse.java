package com.capstone.subway.timetable.dto;

import com.capstone.subway.timetable.DayType;
import com.capstone.subway.timetable.Direction;
import com.capstone.subway.timetable.Timetable;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class TimetableResponse {

    private final Long id;
    private final Long stationId;
    private final String stationName;
    private final Direction direction;
    private final DayType dayType;
    private final LocalTime departureTime;
    private final String trainId;

    public static TimetableResponse from(Timetable timetable) {
        return TimetableResponse.builder()
                .id(timetable.getId())
                .stationId(timetable.getStation().getId())
                .stationName(timetable.getStation().getStationName())
                .direction(timetable.getDirection())
                .dayType(timetable.getDayType())
                .departureTime(timetable.getDepartureTime())
                .trainId(timetable.getTrainId())
                .build();
    }
}
