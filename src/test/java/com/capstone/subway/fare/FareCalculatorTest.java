package com.capstone.subway.fare;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FareCalculatorTest {

    @Mock
    private FarePolicyRepository farePolicyRepository;

    @InjectMocks
    private FareCalculator fareCalculator;

    private List<FarePolicy> policies() {
        return List.of(
                FarePolicy.builder().id(1L).minDistanceKm(0).maxDistanceKm(10).baseFare(1500).build(),
                FarePolicy.builder().id(2L).minDistanceKm(10).maxDistanceKm(30).baseFare(1800).build(),
                FarePolicy.builder().id(3L).minDistanceKm(30).maxDistanceKm(999).baseFare(2200).build()
        );
    }

    @Test
    void picksTheBandContainingTheDistance() {
        when(farePolicyRepository.findAll()).thenReturn(policies());

        assertThat(fareCalculator.calculateFare(5.0)).isEqualTo(1500);
        assertThat(fareCalculator.calculateFare(10.0)).isEqualTo(1800);
        assertThat(fareCalculator.calculateFare(29.9)).isEqualTo(1800);
        assertThat(fareCalculator.calculateFare(40.0)).isEqualTo(2200);
    }

    @Test
    void fallsBackToHighestBandWhenDistanceExceedsAllRanges() {
        List<FarePolicy> narrow = List.of(
                FarePolicy.builder().id(1L).minDistanceKm(0).maxDistanceKm(10).baseFare(1500).build()
        );

        assertThat(fareCalculator.calculateFare(50.0, narrow)).isEqualTo(1500);
    }

    @Test
    void returnsZeroWhenNoPoliciesConfigured() {
        when(farePolicyRepository.findAll()).thenReturn(List.of());

        assertThat(fareCalculator.calculateFare(5.0)).isZero();
    }
}
