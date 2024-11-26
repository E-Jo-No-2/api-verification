package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Spot")
public class SpotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spot_id") // Maps to the primary key in SQL
    private int spot_id;

    @Column(name = "spot_name", nullable = false, length = 100) // Maps to the `name` column in SQL
    private String spot_name;

    @Column(name = "longitude", nullable = false, columnDefinition = "DECIMAL(9,6)") // Matches SQL definition
    private double longitude;

    @Column(name = "latitude", nullable = false, columnDefinition = "DECIMAL(9,6)") // Matches SQL definition
    private double latitude;
}


