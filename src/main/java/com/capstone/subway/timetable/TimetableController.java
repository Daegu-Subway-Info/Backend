package com.capstone.subway.timetable;

import com.capstone.subway.common.ApiResponse;
import com.capstone.subway.timetable.dto.FirstLastTrainResponse;
import com.capstone.subway.timetable.dto.TimetableResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/timetables")
@RequiredArgsConstructor
@Tag(name = "Timetable", description = "Train timetable API")
public class TimetableController {

    private final TimetableService timetableService;

    @GetMapping
    @Operation(summary = "List timetable entries for a station/direction/day type")
    public ApiResponse<List<TimetableResponse>> getTimetables(@RequestParam Long stationId,
                                                                @RequestParam Direction direction,
                                                                @RequestParam DayType dayType) {
        return ApiResponse.success(timetableService.findTimetables(stationId, direction, dayType));
    }

    @GetMapping("/first-last")
    @Operation(summary = "Get first and last train for a station/direction/day type")
    public ApiResponse<FirstLastTrainResponse> getFirstLast(@RequestParam Long stationId,
                                                              @RequestParam Direction direction,
                                                              @RequestParam DayType dayType) {
        return ApiResponse.success(timetableService.findFirstLast(stationId, direction, dayType));
    }

    @GetMapping("/next")
    @Operation(summary = "Get the next train departing after the given time (defaults to now)")
    public ApiResponse<TimetableResponse> getNext(@RequestParam Long stationId,
                                                    @RequestParam Direction direction,
                                                    @RequestParam DayType dayType,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime after) {
        return ApiResponse.success(timetableService.findNext(stationId, direction, dayType, after));
    }
}
