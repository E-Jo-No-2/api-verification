package com.locationbase.dto;

import lombok.Data;

@Data
public class Planner_SpotDTO {
    private int plannerSpotId;
    private int plannerId;
    private String spotName;
    private int visitOrder;
    private int routeId;

}
