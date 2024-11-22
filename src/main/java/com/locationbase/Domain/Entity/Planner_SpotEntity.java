package com.locationbase.Domain.Entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Planner_Spot")
@Data
@NoArgsConstructor
public class Planner_SpotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planner_spot_id", nullable = false)
    private int planner_spot_id;

    @Column(name = " planner_id",nullable = false)
    private int planner_id;

    @Column(name = "spot_id", nullable = false)
    private int spot_id;

    @Column(name = "visit_order", nullable = true)
    private int visit_order;

    public Planner_SpotEntity(int planner_id, int spot_id, int visit_order) {
        this.planner_spot_id = planner_id;
        this.planner_id = planner_id;
        this.spot_id = spot_id;
        this.visit_order = visit_order;
    }

}
