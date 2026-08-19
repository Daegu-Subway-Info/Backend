package com.capstone.subway.fare;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FarePolicyRepository extends JpaRepository<FarePolicy, Long> {

    List<FarePolicy> findAll();
}
