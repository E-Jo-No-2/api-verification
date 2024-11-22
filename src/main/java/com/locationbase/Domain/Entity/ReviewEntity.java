package com.locationbase.Domain.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Table(name = "Review")
@Data
@NoArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private int review_id;

    @Column(name = "user_id", nullable = false, length = 16)
    private String user_id;

    @Column(name = "spot_id", nullable = false)
    private int spot_id;

    @Column(name = "rating", nullable = true,precision = 2, scale = 1 )
    private double rating;

    @Column(name = "comment", nullable = true, columnDefinition = "TEXT")
    private String comment;

    @Column(name = "creat_time", nullable = true )
    private LocalDateTime creat_time;


    public ReviewEntity(String user_id, int spot_id, double rating, String comment, LocalDateTime creat_time) {
        this.user_id = user_id;
        this.spot_id = spot_id;
        this.rating = rating;
        this.comment = comment;
        this.creat_time = creat_time;
    }


}
