package com.locationbase.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "planner_spot")
@NoArgsConstructor
public class PlannerSpotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planner_spot_id")
    private int plannerSpotId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "planner_id", referencedColumnName = "planner_id", nullable = true)
    private PlannerEntity planner;

    @Column(name = "spot_name", nullable = true, length = 255)
    private String spotName;

    @Column(name = "visit_order", nullable = true)
    private Integer visitOrder;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "route_id", referencedColumnName = "route_id", nullable = true)
    private RouteEntity route;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "place_id", referencedColumnName = "place_id", nullable = true)
    private PlacesEntity place;

    @Column(name = "latitude", nullable = false)
    private String latitude;  // 타입 변경: double -> String

    @Column(name = "longitude", nullable = false)
    private String longitude; // 타입 변경: double -> String
}
