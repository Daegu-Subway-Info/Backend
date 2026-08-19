package com.capstone.subway.fare;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "fare_policy")
public class FarePolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fare_policy_id")
    private Long id;

    @Column(name = "min_distance_km", nullable = false)
    private double minDistanceKm;

    @Column(name = "max_distance_km", nullable = false)
    private double maxDistanceKm;

    @Column(name = "base_fare", nullable = false)
    private int baseFare;
}
