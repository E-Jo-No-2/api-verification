package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private PlannerEntity planner_id;

    @Column(name = "write_date", nullable = false)
    private LocalDate write_date;

    @Column(name = "memo_content", columnDefinition = "TEXT", nullable = true)
    private String memo_content;

}