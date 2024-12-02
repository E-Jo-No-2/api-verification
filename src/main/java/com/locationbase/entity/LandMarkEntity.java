package com.locationbase.entity;

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

    @Column(name = "longitude", nullable = false, length = 20) // Now a String
    private String longitude;

    @Column(name = "latitude", nullable = false, length = 20) // Now a String
    private String latitude;
}
