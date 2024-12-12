package com.locationbase.dto;

import lombok.Data;

@Data
public class PlannerSpotDTO {
    private int plannerSpotId;
    private int planner;
    private String spotName;
    private int visitOrder;
    private int routeId;

}
