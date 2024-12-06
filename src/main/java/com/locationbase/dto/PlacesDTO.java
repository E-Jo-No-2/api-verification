package com.locationbase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlacesDTO {
    private int place_id;
    private String lat;
    private String lng;
    private String name;
    private Integer planner_id; // 외래 키는 nullable
}
