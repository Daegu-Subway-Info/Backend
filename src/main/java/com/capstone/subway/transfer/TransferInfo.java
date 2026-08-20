package com.capstone.subway.transfer;

import com.capstone.subway.station.Station;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents the extra time cost of switching lines between two stations
 * that belong to the same transfer complex (e.g. walking between platforms).
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(
        name = "transfer_info",
        indexes = {
                @Index(name = "idx_transfer_from", columnList = "from_station_id"),
                @Index(name = "idx_transfer_to", columnList = "to_station_id")
        }
)
public class TransferInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_station_id", nullable = false)
    private Station fromStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_station_id", nullable = false)
    private Station toStation;

    @Column(name = "transfer_time_sec", nullable = false)
    private int transferTimeSec;
}
