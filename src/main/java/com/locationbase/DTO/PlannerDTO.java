package com.locationbase.DTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class PlannerDTO {
    private int planner_id;
    private String user_id;
    private String theme_name;
    private Date date;

}