package com.capstone.subway.alarm;

import com.capstone.subway.common.ApiResponse;
import com.capstone.subway.alarm.dto.AlarmResponse;
import com.capstone.subway.alarm.dto.CreateAlarmRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
@Tag(name = "Alarm", description = "Get-off alarm API")
public class AlarmController {

    private final AlarmService alarmService;

    @PostMapping
    @Operation(summary = "Create a new alarm")
    public ApiResponse<AlarmResponse> create(@Valid @RequestBody CreateAlarmRequest request) {
        return ApiResponse.success(alarmService.create(request));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active alarm(s) for a device")
    public ApiResponse<List<AlarmResponse>> getActive(@RequestParam String deviceId) {
        return ApiResponse.success(alarmService.findActive(deviceId));
    }

    @DeleteMapping("/{alarmId}")
    @Operation(summary = "Cancel an alarm")
    public ApiResponse<Void> cancel(@PathVariable Long alarmId) {
        alarmService.cancel(alarmId);
        return ApiResponse.success(null);
    }

    @GetMapping("/history")
    @Operation(summary = "Get completed/cancelled alarm history for a device")
    public ApiResponse<List<AlarmResponse>> getHistory(@RequestParam String deviceId) {
        return ApiResponse.success(alarmService.findHistory(deviceId));
    }

    @PostMapping("/{alarmId}/push")
    @Operation(summary = "Demo/manual trigger to push an alarm progress update over STOMP")
    public ApiResponse<AlarmResponse> pushProgress(@PathVariable Long alarmId,
                                                    @RequestParam int remainingStops,
                                                    @RequestParam(required = false) Long remainingSeconds) {
        return ApiResponse.success(alarmService.pushProgress(alarmId, remainingStops, remainingSeconds));
    }
}
