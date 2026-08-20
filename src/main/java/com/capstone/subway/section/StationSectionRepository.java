package com.capstone.subway.section;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationSectionRepository extends JpaRepository<StationSection, Long> {

    List<StationSection> findAll();
}
