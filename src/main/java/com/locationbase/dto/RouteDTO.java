package com.locationbase.dto;

import lombok.Data;

@Data
public class RouteDTO {

    private int route_id;
    private String start_point;
    private String end_point;
    private String theme_name;
    private int taxi_fare;
    private String distance;
    private int estimated_time;

}

