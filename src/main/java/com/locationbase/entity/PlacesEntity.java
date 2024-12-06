package com.locationbase.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "places")
@NoArgsConstructor
public class PlacesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private int place_id; // Primary Key

    @Column(name = "lat", length = 255)
    private String lat;

    @Column(name = "lng", length = 255)
    private String lng;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "planner_id", referencedColumnName = "planner_id", nullable = true)
    private PlannerEntity planner; // planner 테이블과의 외래 키 설정
}
