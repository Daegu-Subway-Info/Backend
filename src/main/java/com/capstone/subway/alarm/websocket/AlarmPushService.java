package com.capstone.subway.alarm.websocket;

import com.capstone.subway.alarm.Alarm;
import com.capstone.subway.alarm.dto.AlarmProgressMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Pushes alarm progress updates over STOMP to two topics per alarm:
 * /topic/alarms/{alarmId} and /topic/alarms/device/{deviceId}.
 *
 * There is no live train-position feed in this system yet, so this is
 * plumbing plus a manual/demo trigger (see AlarmController's push endpoint or
 * call pushAlarmUpdate directly from wherever alarm progress is computed
 * once a real position feed exists).
 */
@Service
@RequiredArgsConstructor
public class AlarmPushService {

    private final SimpMessagingTemplate messagingTemplate;

    public void pushAlarmUpdate(Alarm alarm, int remainingStops, Long remainingSeconds, String message) {
        AlarmProgressMessage payload = AlarmProgressMessage.builder()
                .alarmId(alarm.getId())
                .deviceId(alarm.getDeviceId())
                .targetStationId(alarm.getTargetStation().getId())
                .remainingStops(remainingStops)
                .remainingSeconds(remainingSeconds)
                .status(alarm.getStatus().name())
                .message(message)
                .build();

        messagingTemplate.convertAndSend("/topic/alarms/" + alarm.getId(), payload);
        messagingTemplate.convertAndSend("/topic/alarms/device/" + alarm.getDeviceId(), payload);
    }
}
