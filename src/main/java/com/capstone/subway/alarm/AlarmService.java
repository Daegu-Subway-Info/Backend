package com.capstone.subway.alarm;

import com.capstone.subway.alarm.dto.AlarmResponse;
import com.capstone.subway.alarm.dto.CreateAlarmRequest;
import com.capstone.subway.alarm.websocket.AlarmPushService;
import com.capstone.subway.common.exception.BadRequestException;
import com.capstone.subway.common.exception.NotFoundException;
import com.capstone.subway.station.Station;
import com.capstone.subway.station.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final StationService stationService;
    private final AlarmPushService alarmPushService;

    @Transactional
    public AlarmResponse create(CreateAlarmRequest request) {
        Station station = stationService.getStationOrThrow(request.getTargetStationId());

        Alarm alarm = Alarm.builder()
                .deviceId(request.getDeviceId())
                .targetStation(station)
                .remainingStopsTrigger(request.getRemainingStopsTrigger())
                .status(AlarmStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();
        Alarm saved = alarmRepository.save(alarm);
        return AlarmResponse.from(saved);
    }

    public List<AlarmResponse> findActive(String deviceId) {
        return alarmRepository.findByDeviceIdAndStatusOrderByCreatedAtDesc(deviceId, AlarmStatus.ACTIVE).stream()
                .map(AlarmResponse::from)
                .toList();
    }

    public List<AlarmResponse> findHistory(String deviceId) {
        return alarmRepository.findByDeviceIdAndStatusInOrderByCreatedAtDesc(deviceId, List.of(AlarmStatus.COMPLETED, AlarmStatus.CANCELLED))
                .stream()
                .map(AlarmResponse::from)
                .toList();
    }

    @Transactional
    public void cancel(Long alarmId) {
        Alarm alarm = getAlarmOrThrow(alarmId);
        if (alarm.getStatus() != AlarmStatus.ACTIVE) {
            throw new BadRequestException("Only active alarms can be cancelled");
        }
        alarm.cancel();
        alarmPushService.pushAlarmUpdate(alarm, 0, null, "Alarm cancelled");
    }

    /**
     * Manual/demo trigger to push a progress update for an active alarm and,
     * once remainingStops reaches 0, mark it completed. Intended to be called
     * from wherever real train-position progress would be computed once such
     * a feed exists; for now it can be invoked directly (e.g. from a demo endpoint).
     */
    @Transactional
    public AlarmResponse pushProgress(Long alarmId, int remainingStops, Long remainingSeconds) {
        Alarm alarm = getAlarmOrThrow(alarmId);
        if (alarm.getStatus() != AlarmStatus.ACTIVE) {
            throw new BadRequestException("Alarm is not active");
        }

        if (remainingStops <= 0) {
            alarm.complete();
            alarmPushService.pushAlarmUpdate(alarm, 0, remainingSeconds, "Arrived at target station");
        } else {
            String message = remainingStops <= alarm.getRemainingStopsTrigger()
                    ? "Approaching target station"
                    : "En route";
            alarmPushService.pushAlarmUpdate(alarm, remainingStops, remainingSeconds, message);
        }
        return AlarmResponse.from(alarm);
    }

    private Alarm getAlarmOrThrow(Long alarmId) {
        return alarmRepository.findById(alarmId)
                .orElseThrow(() -> new NotFoundException("Alarm not found: " + alarmId));
    }
}
