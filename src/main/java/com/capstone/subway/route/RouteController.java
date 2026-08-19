package com.capstone.subway.route;

import com.capstone.subway.common.ApiResponse;
import com.capstone.subway.route.dto.RouteFareResponse;
import com.capstone.subway.route.dto.RouteRequest;
import com.capstone.subway.route.dto.RouteResponse;
import com.capstone.subway.route.dto.RouteTimeResponse;
import com.capstone.subway.station.dto.StationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
@Tag(name = "Route", description = "Shortest route computation API")
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    @Operation(summary = "Compute shortest route between two stations")
    public ApiResponse<RouteResponse> getRoute(@Valid @RequestBody RouteRequest request) {
        RouteResult result = routeService.findShortestRoute(request.getFromStationId(), request.getToStationId());
        int fare = routeService.fareFor(result.getTotalDistanceKm());
        RouteResponse response = RouteResponse.builder()
                .path(result.getPath().stream().map(StationResponse::from).toList())
                .totalDistanceKm(result.getTotalDistanceKm())
                .totalDurationSeconds(result.getTotalDurationSeconds())
                .transferCount(result.getTransferCount())
                .fare(fare)
                .build();
        return ApiResponse.success(response);
    }

    @PostMapping("/fare")
    @Operation(summary = "Compute fare between two stations")
    public ApiResponse<RouteFareResponse> getFare(@Valid @RequestBody RouteRequest request) {
        RouteResult result = routeService.findShortestRoute(request.getFromStationId(), request.getToStationId());
        int fare = routeService.fareFor(result.getTotalDistanceKm());
        return ApiResponse.success(RouteFareResponse.builder()
                .totalDistanceKm(result.getTotalDistanceKm())
                .fare(fare)
                .build());
    }

    @PostMapping("/time")
    @Operation(summary = "Compute travel time and transfer count between two stations")
    public ApiResponse<RouteTimeResponse> getTime(@Valid @RequestBody RouteRequest request) {
        RouteResult result = routeService.findShortestRoute(request.getFromStationId(), request.getToStationId());
        return ApiResponse.success(RouteTimeResponse.builder()
                .totalDurationSeconds(result.getTotalDurationSeconds())
                .transferCount(result.getTransferCount())
                .build());
    }
}
