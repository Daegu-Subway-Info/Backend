package com.capstone.subway.timetable;

import com.capstone.subway.common.exception.NotFoundException;
import com.capstone.subway.timetable.dto.FirstLastTrainResponse;
import com.capstone.subway.timetable.dto.TimetableResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimetableService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    private final TimetableRepository timetableRepository;

    public List<TimetableResponse> findTimetables(Long stationId, Direction direction, DayType dayType) {
        return timetableRepository
                .findByStationIdAndDirectionAndDayTypeOrderByDepartureTimeAsc(stationId, direction, dayType)
                .stream()
                .map(TimetableResponse::from)
                .toList();
    }

    public FirstLastTrainResponse findFirstLast(Long stationId, Direction direction, DayType dayType) {
        TimetableResponse first = timetableRepository
                .findFirstByStationIdAndDirectionAndDayTypeOrderByDepartureTimeAsc(stationId, direction, dayType)
                .map(TimetableResponse::from)
                .orElseThrow(() -> new NotFoundException("No timetable found for station " + stationId));
        TimetableResponse last = timetableRepository
                .findFirstByStationIdAndDirectionAndDayTypeOrderByDepartureTimeDesc(stationId, direction, dayType)
                .map(TimetableResponse::from)
                .orElseThrow(() -> new NotFoundException("No timetable found for station " + stationId));
        return FirstLastTrainResponse.builder().firstTrain(first).lastTrain(last).build();
    }

    public TimetableResponse findNext(Long stationId, Direction direction, DayType dayType, LocalTime after) {
        LocalTime reference = after != null ? after : LocalTime.now(SEOUL);
        return timetableRepository
                .findFirstByStationIdAndDirectionAndDayTypeAndDepartureTimeGreaterThanEqualOrderByDepartureTimeAsc(
                        stationId, direction, dayType, reference)
                .map(TimetableResponse::from)
                .orElseThrow(() -> new NotFoundException("No upcoming train found for station " + stationId));
    }
}
