package com.locationbase.DTO;

import lombok.Data;

@Data
public class Planner_SpotDTO {
    private int plannerSpotId;
    private int planner;
    private String spot_name;
    private int visit_order;
    private int route_id;

}
