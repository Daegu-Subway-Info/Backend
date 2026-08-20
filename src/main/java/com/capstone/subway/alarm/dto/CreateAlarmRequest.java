package com.capstone.subway.alarm.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlarmRequest {

    @NotNull
    private Long targetStationId;

    @Min(0)
    private int remainingStopsTrigger;

    @NotBlank
    private String deviceId;
}
