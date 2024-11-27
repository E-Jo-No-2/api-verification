package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Spot")
@NoArgsConstructor
public class SpotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spot_id")
    private int spotId;

    @Column(name = "Spot_name", nullable = false, length = 50)
    private String spotName;

    @Column(name = "longitude", nullable = false, columnDefinition = "DECIMAL(9,6)")
    private double longitude;

    @Column(name = "latitude", nullable = false, columnDefinition = "DECIMAL(9,6)")
    private double latitude;
}
