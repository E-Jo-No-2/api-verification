package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Memo")
@NoArgsConstructor
public class MemoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private int memo_id;

    @ManyToOne
    @JoinColumn(name = "planner_id", referencedColumnName = "planner_id", nullable = false)
    private PlannerEntity planner_id; // Assuming PlannerEntity has a `planner_id` field

    @Column(name = "memo_content", columnDefinition = "TEXT", nullable = true)
    private String memo_content;

}