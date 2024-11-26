package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "Planner")
@NoArgsConstructor
public class PlannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planner_id")
    private int planner_id; // Primary Key (planner_id)

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user_id; // User reference (user_id)

    @Column(name = "start_date")
    private LocalDate start_date; // Start date of the planner

    @ManyToOne
    @JoinColumn(name = "theme_name", referencedColumnName = "theme_name")
    private ThemeEntity theme_name; // Theme reference (theme_name)

    @Column(name = "date", nullable = false)
    private LocalDate date; // Date of the planner entry

    @ManyToOne
    @JoinColumn(name = "landmark_name", referencedColumnName = "landmark_name")
    private LandMarkEntity landmark_name; // Landmark reference (landmark_name)

    @ManyToOne
    @JoinColumn(name = "date", referencedColumnName = "date", insertable = false, updatable = false)
    private WeatherEntity weather; // Weather reference (date)


    public PlannerEntity(UserEntity user, LocalDate startDate, ThemeEntity themeName, LocalDate date, LandMarkEntity landmarkName) {
        this.user_id = user;
        this.start_date = start_date;
        this.theme_name = theme_name;
        this.date = date;
        this.landmark_name = landmark_name;
    }
}
