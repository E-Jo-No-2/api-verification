package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Landmark")
@NoArgsConstructor
public class LandMarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "landmark_id")
    private int landmark_id;

    @Column(name = "landmark_name", nullable = false, length = 50, unique = true)
    private String landmark_name;

    @Column(name = "theme_name", nullable = false, length = 30)
    private String theme_name;

    @Column(name = "longitude", nullable = false, columnDefinition = "DECIMAL(9,6)")
    private double longitude;

    @Column(name = "latitude", nullable = false, columnDefinition = "DECIMAL(9,6)")
    private double latitude;

    public LandMarkEntity(String landmark_name, String theme_name, double longitude, double latitude) {
        this.landmark_name = landmark_name;
        this.theme_name = theme_name;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
