package com.locationbase.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class MemoDTO {
    private int memo_id;
    private int planner_id;
    private Date write_date;
    private String memo_content;

}
