package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Planner_Spot")
@NoArgsConstructor
public class Planner_SpotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planner_spot_id")
    private int planner_spot_id;

    @ManyToOne
    @JoinColumn(name = "planner_id", referencedColumnName = "planner_id", nullable = true)
    private PlannerEntity planner_id;

    @Column(name = "spot_name", nullable = true, length = 255)
    private String spot_name;

    @Column(name = "visit_order", nullable = true)
    private int visit_order;

    @Column(name = "route_id", nullable = true)
    private int route_id;

}
