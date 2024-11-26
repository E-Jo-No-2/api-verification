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

    @ManyToOne
    @JoinColumn(name = "spot_id", referencedColumnName = "spot_id", nullable = true)
    private SpotEntity spot_id;

    @Column(name = "visit_order")
    private int visit_order;

}
