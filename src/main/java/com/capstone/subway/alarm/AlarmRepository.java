package com.capstone.subway.alarm;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findByDeviceIdAndStatusOrderByCreatedAtDesc(String deviceId, AlarmStatus status);

    List<Alarm> findByDeviceIdAndStatusInOrderByCreatedAtDesc(String deviceId, List<AlarmStatus> statuses);
}
