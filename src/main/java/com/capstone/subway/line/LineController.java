package com.capstone.subway.line;

import com.capstone.subway.common.ApiResponse;
import com.capstone.subway.line.dto.LineDetailResponse;
import com.capstone.subway.line.dto.LineResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lines")
@RequiredArgsConstructor
@Tag(name = "Line", description = "Subway line API")
public class LineController {

    private final LineService lineService;

    @GetMapping
    @Operation(summary = "List all lines")
    public ApiResponse<List<LineResponse>> getLines() {
        return ApiResponse.success(lineService.findAll());
    }

    @GetMapping("/{lineId}")
    @Operation(summary = "Get line detail including its stations")
    public ApiResponse<LineDetailResponse> getLineDetail(@PathVariable Long lineId) {
        return ApiResponse.success(lineService.findDetail(lineId));
    }
}
