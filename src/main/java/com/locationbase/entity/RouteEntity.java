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
    private int route_id; // Primary Key

    @ManyToOne
    @JoinColumn(name = "start_point", referencedColumnName = "place_id", nullable = false)
    private PlacesEntity start_point; // 외래 키 설정

    @ManyToOne
    @JoinColumn(name = "end_point", referencedColumnName = "place_id", nullable = true)
    private PlacesEntity end_point; // 외래 키 설정, nullable

    @Column(name = "theme_name", nullable = false, length = 255)
    private String themeName;

    @Column(name = "taxi_fare", nullable = true)
    private Integer taxi_fare; // int형은 기본값이 0이므로, Integer로 nullable 설정

    @Column(name = "estimated_time")
    private Integer estimated_time; // int형은 기본값이 0이므로, Integer로 nullable 설정

    @Column(name = "distance", length = 10)
    private String distance;
}
