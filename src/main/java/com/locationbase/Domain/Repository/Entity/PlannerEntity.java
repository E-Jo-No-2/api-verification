package com.locationbase.Domain.Repository.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "planner")
public class PlannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // planner_id 자동 생성
    private int plannerId;

    @Column(nullable = false, length = 36)
    private String userId;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false, length = 50)
    private String themeName;

    // Getters and Setters
    public int getPlannerId() {
        return plannerId;
    }

    public void setPlannerId(int plannerId) {
        this.plannerId = plannerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
}