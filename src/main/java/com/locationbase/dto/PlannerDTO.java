package com.locationbase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PlannerDTO {
    private int planner_id;
    private String user_id; // 외래 키를 참조
    private Date date;
}
