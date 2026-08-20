package com.capstone.subway.alarm;

import com.capstone.subway.station.Station;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A single alarm request from a client device. History is not modeled as a
 * separate table — completed/cancelled alarms simply remain in this table
 * with their status changed, and history queries filter on status.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "alarm",
        indexes = {
                @Index(name = "idx_alarm_device_status", columnList = "device_id, status")
        }
)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_station_id", nullable = false)
    private Station targetStation;

    @Column(name = "remaining_stops_trigger", nullable = false)
    private int remainingStopsTrigger;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AlarmStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public void cancel() {
        this.status = AlarmStatus.CANCELLED;
        this.completedAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = AlarmStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
}
