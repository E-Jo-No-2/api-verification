package com.locationbase.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "route") // 테이블명 소문자로 수정
@NoArgsConstructor
public class RouteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private int routeId; // Primary Key

    @ManyToOne
    @JoinColumn(name = "start_point", referencedColumnName = "place_id", nullable = false)
    private PlacesEntity startPoint; // 외래 키 설정

    @ManyToOne
    @JoinColumn(name = "end_point", referencedColumnName = "place_id", nullable = true)
    private PlacesEntity endPoint; // 외래 키 설정, nullable

    @Column(name = "theme_name", nullable = false, length = 255)
    private String themeName;

    @Column(name = "taxi_fare", nullable = true)
    private Integer taxiFare; // int형은 기본값이 0이므로, Integer로 nullable 설정

    @Column(name = "estimated_time")
    private Integer estimatedTime; // int형은 기본값이 0이므로, Integer로 nullable 설정

    @Column(name = "distance", length = 10)
    private String distance;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "planner_id", referencedColumnName = "planner_id", nullable = true)
    private PlannerEntity planner; // planner 테이블과의 외래 키 설정
}
