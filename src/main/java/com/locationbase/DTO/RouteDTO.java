package com.locationbase.DTO;

import lombok.Data;

import java.time.LocalTime;

@Data
public class RouteDTO {

    private int route_id;
    private String start_point;
    private String end_point;
    private int taxi_fare;
    private String distance;
    private int estimated_time;

}

