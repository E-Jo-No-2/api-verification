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
    private int plannerId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userId;  // UserEntity와 관계 설정

    @Column(name = "theme_name", length = 50, nullable = true)
    private String themeName;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    public PlannerEntity(UserEntity userId, LocalDate date, String themeName) {
        this.userId = userId;
        this.date = date;
        this.themeName = themeName;
    }
}
