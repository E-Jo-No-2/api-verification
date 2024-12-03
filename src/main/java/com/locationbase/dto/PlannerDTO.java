package com.locationbase.dto;
import lombok.Data;

import java.util.Date;

@Data
public class PlannerDTO {
    private int plannerId;
    private String user_id;
    private Date date;


}