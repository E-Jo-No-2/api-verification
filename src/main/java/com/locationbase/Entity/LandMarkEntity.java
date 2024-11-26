package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Landmark")
public class LandMarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "landmark_id") // Explicit mapping to match database column
    private int landmark_id;

    @Column(name = "landmark_name", nullable = false, length = 100) // Explicit mapping
    private String landmark_name;

    @Column(name = "theme_name", nullable = false, length = 50) // Explicit mapping
    private String theme_name;

    @Column(name = "longitude", nullable = false, columnDefinition = "DECIMAL(9,6)") // Precision for decimal
    private double longitude;

    @Column(name = "latitude", nullable = false, columnDefinition = "DECIMAL(9,6)") // Precision for decimal
    private double latitude;
}
