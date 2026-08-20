package com.capstone.subway.timetable;

import com.capstone.subway.station.Station;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "timetable",
        indexes = {
                @Index(name = "idx_timetable_station_dir_day", columnList = "station_id, direction, day_type"),
                @Index(name = "idx_timetable_departure", columnList = "departure_time")
        }
)
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false, length = 20)
    private Direction direction;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_type", nullable = false, length = 20)
    private DayType dayType;

    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    @Column(name = "train_id", length = 50)
    private String trainId;
}
