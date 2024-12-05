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
    private int taxi_fare;
    private String distance;
    private int estimated_time;
}
