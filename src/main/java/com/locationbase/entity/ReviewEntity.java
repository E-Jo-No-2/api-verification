package com.locationbase.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Review",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "place_id"})})
@NoArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private PlacesEntity place;

    @Column(name = "rating", precision = 2, scale = 1)
    private Double rating;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "creat_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createTime;

    @Column(name = "longitude", nullable = false, length = 20)
    private String lng;

    @Column(name = "latitude", nullable = false, length = 20)
    private String lat;
}
