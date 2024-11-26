package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Planner")
@Data
@NoArgsConstructor
public class PlannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planner_id")
    private int planner_id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user_id;  // UserEntity와 관계 설정

    @Column(name = "start_date", nullable = false)
    private LocalDate start_date;

    @Column(name = "theme_name", length = 50, nullable = false)
    private String theme_name;

    public PlannerEntity(UserEntity user_id, LocalDate start_date, String theme_name) {
        this.user_id = user_id;
        this.start_date = start_date;
        this.theme_name = theme_name;
    }

}
