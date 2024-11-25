package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Spot")
public class SpotEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int spot_id;

    @Column(nullable = false,length = 50)
    private String spot_name;


    @Column(nullable = false, precision = 9, scale = 6)
    private double longitude;


    @Column(nullable = false, precision = 9, scale = 6)
    private double latitude;


}

