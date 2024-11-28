package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Route")
@NoArgsConstructor
public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private int route_id; // Primary Key

    @Column(name = "start_point", nullable = false, length = 255)
    private String start_point;

    @Column(name = "end_point", nullable = false, length = 255)
    private String end_point;

    @Column(name = "taxi_fare",nullable = true)
    private int taxi_fare;

    @Column(name = "estimated_time")
    private int estimated_time;

    @Column(name = "distance", length = 10)
    private String distance;
}
