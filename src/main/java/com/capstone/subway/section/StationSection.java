package com.capstone.subway.section;

import com.capstone.subway.station.Station;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a directed edge between two adjacent stations on the same line.
 * Sections are stored one-directionally (from -> to) but are treated as an
 * undirected graph at query/traversal time (RouteService considers both
 * directions when building the graph), since Daegu subway lines allow travel
 * in both directions along the same physical track.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "station_section",
        indexes = {
                @Index(name = "idx_section_from", columnList = "from_station_id"),
                @Index(name = "idx_section_to", columnList = "to_station_id")
        }
)
public class StationSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_station_id", nullable = false)
    private Station fromStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_station_id", nullable = false)
    private Station toStation;

    @Column(name = "duration_seconds", nullable = false)
    private int durationSeconds;

    @Column(name = "distance_km", nullable = false)
    private double distanceKm;
}
