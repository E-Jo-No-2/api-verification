package com.locationbase.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Planner_Spot")
@NoArgsConstructor
public class PlannerSpotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planner_spot_id")
    private int plannerSpotId;

    @ManyToOne
    @JoinColumn(name = "planner_id", referencedColumnName = "planner_id", nullable = true)
    private PlannerEntity planner;

    @Column(name = "spot_name", nullable = true, length = 255)
    private String spotName;

    @Column(name = "visit_order", nullable = true)
    private int visitOrder;

    @Column(name = "route_id", nullable = true)
    private int routeId;

}