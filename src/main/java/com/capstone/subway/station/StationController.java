package com.capstone.subway.station;

import com.capstone.subway.common.ApiResponse;
import com.capstone.subway.station.dto.StationResponse;
import com.capstone.subway.station.dto.StationSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
@RequiredArgsConstructor
@Tag(name = "Station", description = "Subway station API")
public class StationController {

    private final StationService stationService;

    @GetMapping
    @Operation(summary = "List all stations")
    public ApiResponse<List<StationResponse>> getStations() {
        return ApiResponse.success(stationService.findAll());
    }

    @GetMapping("/{stationId}")
    @Operation(summary = "Get station by id")
    public ApiResponse<StationResponse> getStation(@PathVariable Long stationId) {
        return ApiResponse.success(stationService.findById(stationId));
    }

    @GetMapping("/search")
    @Operation(summary = "Search stations by name keyword")
    public ApiResponse<StationSearchResponse> search(@RequestParam String keyword) {
        return ApiResponse.success(stationService.search(keyword));
    }
}
