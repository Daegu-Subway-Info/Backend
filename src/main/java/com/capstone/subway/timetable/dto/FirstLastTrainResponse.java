package com.capstone.subway.timetable.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FirstLastTrainResponse {

    private final TimetableResponse firstTrain;
    private final TimetableResponse lastTrain;
}
