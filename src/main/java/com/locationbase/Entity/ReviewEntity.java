package com.locationbase.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Review")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int review_id;

    @Column(nullable = false, length = 16)
    private String user_id;

    @Column(nullable = false)
    private int spot_id;

    @Column(nullable = true, precision = 2, scale = 1)
    private double rating;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = true)
    private LocalDateTime creat_time;

}
