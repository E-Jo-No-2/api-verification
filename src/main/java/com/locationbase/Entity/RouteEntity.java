/*package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Entity
@Table(name = "Route")
@NoArgsConstructor
public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private int route_id;


    @Column(name = "planner_id", nullable = false)
    private int planner_id;

    @OneToOne
    @JoinColumn(name = "start_spot_id", unique = true, nullable = false)
    private SpotEntity start_spot_id;

    @OneToOne
    @JoinColumn(name = "end_spot_id", unique = true, nullable = false)
    private SpotEntity end_spot_id;

    @Column(name = "transport_mode", length = 50, nullable = true)
    private String transport_mode;

    @Column(name = "distance", precision = 5, scale = 2, nullable = true)
    private Double distance;

    @Column(name = "estimated_time", nullable = true)
    private LocalTime estimated_time;

    public RouteEntity(
            int planner_id,
            SpotEntity start_spot_id,
            SpotEntity end_spot_id,
            String transport_mode,
            Double distance,
            LocalTime estimated_time
    ) {
        this.planner_id = planner_id;
        this.start_spot_id = start_spot_id;
        this.end_spot_id = end_spot_id;
        this.transport_mode = transport_mode;
        this.distance = distance;
        this.estimated_time = estimated_time;
    }
}*/
