package com.locationbase.Domain.Repository.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "planner")
public class PlannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // planner_id 자동 생성
    private int planner_id;

    @Column(nullable = false, length = 36)
    private String user_id;

    @Column(nullable = false)
    private LocalDate start_date;

    @Column(nullable = false, length = 50)
    private String theme_name;
}