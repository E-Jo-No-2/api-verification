package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "Planner")
public class PlannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // planner_id 자동 생성
    private int planner_id;

    @Column(nullable = true, length = 36)
    private String user_id;

    @Column(nullable = true)
    private LocalDate start_date;

    @Column(nullable = true, length = 50)
    private String theme_name;
}