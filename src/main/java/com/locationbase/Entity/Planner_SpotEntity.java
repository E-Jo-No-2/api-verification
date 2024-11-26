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
    private int planner_spot_id;

    @Column(nullable = false)
    private int planner_id;

    @Column(nullable = false)
    private int spot_id;

    @Column(nullable = true)
    private int visit_order;

    public Planner_SpotEntity(int planner_id, int spot_id, Integer visit_order) {
        this.planner_id = planner_id;
        this.spot_id = spot_id;
        this.visit_order = visit_order;
    }
}
