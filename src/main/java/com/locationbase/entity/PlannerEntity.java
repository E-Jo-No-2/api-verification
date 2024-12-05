package com.locationbase.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "planner") // 'planner' 테이블과 매핑
@Data
@NoArgsConstructor
public class PlannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planner_id")
    private int plannerId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;  // 'user_id' 컬럼과 UserEntity와 관계 설정

    @Column(name = "date", nullable = false)
    private LocalDate date;

    public PlannerEntity(UserEntity user, LocalDate date) {
        this.user = user;
        this.date = date;
    }
}
