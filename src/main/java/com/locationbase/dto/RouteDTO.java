package com.locationbase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RouteDTO {
    private int route_id;
    private int start_point; // Place의 ID를 참조
    private Integer end_point; // Place의 ID를 참조, nullable
    private String theme_name;
    private Integer taxi_fare; // int형은 기본값이 0이므로, Integer로 nullable 설정
    private String distance;
    private Integer estimated_time; // int형은 기본값이 0이므로, Integer로 nullable 설정
    private Integer plannerId; // Planner의 ID를 참조, nullable


}
