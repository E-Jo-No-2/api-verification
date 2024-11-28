package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Review",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "spot_id"})})
@NoArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int review_id; // Primary Key

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Foreign Key to User
    private UserEntity user_id; // Reference to UserEntity

    @Column(name = "rating", length = 5)
    private String rating; // Rating stored as String

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment; // Review comment

    @Column(name = "creat_time", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime create_time; // Timestamp when the review was created

    @Column(name = "longitude", nullable = false, length = 20)
    private String longitude; // Longitude of the review spot

    @Column(name = "latitude", nullable = false, length = 20)
    private String latitude; // Latitude of the review spot

    @Column(name = "spot_name", nullable = false, length = 50)
    private String spot_name; // Name of the reviewed spot


}
