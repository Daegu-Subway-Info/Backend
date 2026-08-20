package com.capstone.subway.timetable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    List<Timetable> findByStationIdAndDirectionAndDayTypeOrderByDepartureTimeAsc(
            Long stationId, Direction direction, DayType dayType);

    Optional<Timetable> findFirstByStationIdAndDirectionAndDayTypeOrderByDepartureTimeAsc(
            Long stationId, Direction direction, DayType dayType);

    Optional<Timetable> findFirstByStationIdAndDirectionAndDayTypeOrderByDepartureTimeDesc(
            Long stationId, Direction direction, DayType dayType);

    Optional<Timetable> findFirstByStationIdAndDirectionAndDayTypeAndDepartureTimeGreaterThanEqualOrderByDepartureTimeAsc(
            Long stationId, Direction direction, DayType dayType, LocalTime time);
}
