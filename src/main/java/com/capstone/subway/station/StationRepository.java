package com.capstone.subway.station;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {

    List<Station> findByLineIdOrderBySequenceNoAsc(Long lineId);

    List<Station> findByStationNameContainingIgnoreCaseOrderByStationNameAsc(String keyword);
}
