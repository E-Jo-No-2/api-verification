package com.locationbase.DTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class PlannerDTO {
    private int plannerId;
    private String user_id;
    private Date date;

}