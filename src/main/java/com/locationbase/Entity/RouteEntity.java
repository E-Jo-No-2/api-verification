package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "Route")
@NoArgsConstructor
public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private int route_id; // Primary Key (route_id)

    @ManyToOne
    @JoinColumn(name = "planner_id", referencedColumnName = "planner_id")
    private PlannerEntity planner_id; // Planner reference (planner_id)

    @ManyToOne
    @JoinColumn(name = "start_spot_id", referencedColumnName = "spot_id")
    private SpotEntity start_spot_id; // Start Spot reference (start_spot_id)

    @ManyToOne
    @JoinColumn(name = "end_spot_id", referencedColumnName = "spot_id")
    private SpotEntity end_spot_id; // End Spot reference (end_spot_id)

    @Column(name = "transport_mode", length = 20)
    private String transport_mode; // Mode of transportation

    @Column(name = "distance", precision = 5, scale = 2)
    private BigDecimal distance; // Distance (e.g., in kilometers)

    @Column(name = "estimated_time")
    private LocalTime estimated_time; // Estimated time for the route

    // Constructor for RouteEntity
    public RouteEntity(PlannerEntity planner, SpotEntity startSpot, SpotEntity endSpot, String transportMode, BigDecimal distance, LocalTime estimatedTime) {
        this.planner_id = planner;
        this.start_spot_id = startSpot;
        this.end_spot_id = endSpot;
        this.transport_mode = transportMode;
        this.distance = distance;
        this.estimated_time = estimatedTime;
    }
}
