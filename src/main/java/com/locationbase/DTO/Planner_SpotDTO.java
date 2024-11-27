package com.locationbase.DTO;

import lombok.Data;

@Data
public class Planner_SpotDTO {
    private int planner_spot_id;
    private int planner_id;
    private String spot_name;
    private int visit_order;
    private int route_id;

}
