package com.locationbase.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "planner")
@NoArgsConstructor
public class PlannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planner_id")
    private int plannerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity userId;  // UserEntity와 관계 설정

    @Column(name = "date", nullable = false)
    private LocalDate date;

    // 플래너 완료 여부
    @Column(name = "completed", nullable = true)
    private boolean completed;

    // Getters and Setters
    @JsonProperty("completed")
    public boolean isCompleted() {
        return completed;
    }
    @JsonProperty("completed")
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @OneToMany(mappedBy = "planner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlacesEntity> places;
}
