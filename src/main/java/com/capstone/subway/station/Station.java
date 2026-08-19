package com.capstone.subway.station;

import com.capstone.subway.line.Line;
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
@Table(
        name = "station",
        indexes = {
                @Index(name = "idx_station_name", columnList = "station_name"),
                @Index(name = "idx_station_line", columnList = "line_id")
        }
)
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "station_id")
    private Long id;

    @Column(name = "station_name", nullable = false, length = 100)
    private String stationName;

    @Column(name = "station_code", unique = true, length = 20)
    private String stationCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    @Column(name = "sequence_no")
    private Integer sequenceNo;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;
}
