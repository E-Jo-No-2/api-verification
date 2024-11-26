package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Memo")
public class MemoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memo_id;

    @Column(nullable = false)
    private int planner_id;

    @Column(nullable = false)
    private String memo_content;

}
