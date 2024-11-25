package com.locationbase.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Planner_Spot")
public class Planner_SpotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int planner_spot_id;

    @Column(nullable = false)
    private int planner_id;

    @Column(nullable = false)
    private int spot_id;

    @Column(nullable = true)
    private int visit_order;
}
