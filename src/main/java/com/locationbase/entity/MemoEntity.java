package com.locationbase.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;


import java.time.LocalDate;

@Data
@Entity
@Table(name = "Memo")
@NoArgsConstructor
public class MemoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    private int memoId;

    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "planner_id", referencedColumnName = "planner_id", nullable = false)
    @JsonBackReference //Prevent recursion in JSON serialization
    private PlannerEntity planner;

    @Column(name = "write_date", nullable = false)
    private LocalDate write_date;

    @Column(name = "memo_content", columnDefinition = "TEXT", nullable = true)
    private String memoContent;


}