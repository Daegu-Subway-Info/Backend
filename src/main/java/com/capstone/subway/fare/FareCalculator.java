package com.capstone.subway.fare;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * Calculates fare for a given distance using FarePolicy bands
 * (min_distance_km inclusive, max_distance_km exclusive except for the last band).
 * Distances below the lowest band's minimum fall back to the cheapest band;
 * distances above the highest band's maximum (or falling in a configured gap
 * between bands) fall back to the nearest band by distance. Returns 0 if no
 * policies are configured.
 */
@Component
@RequiredArgsConstructor
public class FareCalculator {

    private final FarePolicyRepository farePolicyRepository;

    public int calculateFare(double distanceKm) {
        List<FarePolicy> policies = farePolicyRepository.findAll();
        return calculateFare(distanceKm, policies);
    }

    public int calculateFare(double distanceKm, List<FarePolicy> policies) {
        if (policies == null || policies.isEmpty()) {
            return 0;
        }
        return policies.stream()
                .filter(p -> distanceKm >= p.getMinDistanceKm() && distanceKm < p.getMaxDistanceKm())
                .findFirst()
                .map(FarePolicy::getBaseFare)
                .orElseGet(() -> nearestBandFare(distanceKm, policies));
    }

    private int nearestBandFare(double distanceKm, List<FarePolicy> policies) {
        return policies.stream()
                .min(Comparator.comparingDouble(p -> distanceTo(distanceKm, p)))
                .map(FarePolicy::getBaseFare)
                .orElse(0);
    }

    private double distanceTo(double distanceKm, FarePolicy policy) {
        if (distanceKm < policy.getMinDistanceKm()) {
            return policy.getMinDistanceKm() - distanceKm;
        }
        if (distanceKm >= policy.getMaxDistanceKm()) {
            return distanceKm - policy.getMaxDistanceKm();
        }
        return 0;
    }
}
