package com.capstone.subway.alarm.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * Payload pushed over STOMP to subscribers of an alarm's progress topic.
 */
@Getter
@Builder
public class AlarmProgressMessage {

    private final Long alarmId;
    private final String deviceId;
    private final Long targetStationId;
    private final int remainingStops;
    private final Long remainingSeconds;
    private final String status;
    private final String message;
}
