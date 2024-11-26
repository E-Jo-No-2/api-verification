package com.locationbase.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Review")
@NoArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int review_id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private UserEntity user; // Assuming UserEntity has a user_id field

    @ManyToOne
    @JoinColumn(name = "spot_id", referencedColumnName = "spot_id", nullable = false)
    private SpotEntity spot; // Assuming SpotEntity has a spot_id field

    @Column(name = "rating")
    private Double rating; // Using Double without precision and scale

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "creat_time", nullable = true, updatable = false)
    private LocalDateTime creat_time;

    @PrePersist
    protected void onCreate() {
        if (this.creat_time == null) {
            this.creat_time = LocalDateTime.now(); // Auto-set timestamp if not provided
        }
    }
}
