package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Review")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int review_id;

    @Column(name = "user_id", nullable = false)
    private int user_id;

    @Column(name = "spot_id", nullable = false)
    private int spot_id;

    @Column(name = "rating", nullable = true)
    private Double rating;

    @Column(name = "comment", columnDefinition = "TEXT", nullable = false)
    private String comment;

    @Column(name = "creat_time", nullable = true, updatable = false)
    private LocalDateTime creat_time;

    @PrePersist
    protected void onCreate() {
        this.creat_time = LocalDateTime.now(); // Auto-set timestamp
    }
}

