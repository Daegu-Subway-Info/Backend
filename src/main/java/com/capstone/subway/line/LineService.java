package com.capstone.subway.line;

import com.capstone.subway.common.exception.NotFoundException;
import com.capstone.subway.line.dto.LineDetailResponse;
import com.capstone.subway.line.dto.LineResponse;
import com.capstone.subway.station.Station;
import com.capstone.subway.station.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public List<LineResponse> findAll() {
        return lineRepository.findAll().stream().map(LineResponse::from).toList();
    }

    public LineDetailResponse findDetail(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException("Line not found: " + lineId));
        List<Station> stations = stationRepository.findByLineIdOrderBySequenceNoAsc(lineId);
        return LineDetailResponse.of(line, stations);
    }
}
