package com.capstone.subway.alarm.dto;

import com.capstone.subway.alarm.Alarm;
import com.capstone.subway.alarm.AlarmStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AlarmResponse {

    private final Long id;
    private final String deviceId;
    private final Long targetStationId;
    private final String targetStationName;
    private final int remainingStopsTrigger;
    private final AlarmStatus status;
    private final LocalDateTime createdAt;
    private final LocalDateTime completedAt;

    public static AlarmResponse from(Alarm alarm) {
        return AlarmResponse.builder()
                .id(alarm.getId())
                .deviceId(alarm.getDeviceId())
                .targetStationId(alarm.getTargetStation().getId())
                .targetStationName(alarm.getTargetStation().getStationName())
                .remainingStopsTrigger(alarm.getRemainingStopsTrigger())
                .status(alarm.getStatus())
                .createdAt(alarm.getCreatedAt())
                .completedAt(alarm.getCompletedAt())
                .build();
    }
}
