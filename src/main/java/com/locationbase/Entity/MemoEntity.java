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
    private int memo_id;

    @Column(nullable = false)
    private int planner_id;

    @Column(nullable = false)
    private String memo_content;

    public MemoEntity(int planner_id, String memo_content) {
        this.planner_id = planner_id;
        this.memo_content = memo_content;
    }

}
