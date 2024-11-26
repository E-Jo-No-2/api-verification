package com.locationbase.DTO;

import lombok.Data;

import java.time.LocalTime;

@Data
public class RouteDTO {

    private int route_id;
    private int planner_id;
    private int start_spot_id;
    private int end_spot_id;
    private String transport_mode;
    private double distance;
    private LocalTime estimated_time;

}

