package com.locationbase.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MemoDTO {
    private int memoId;
    private int planner;
    private Date write_date;
    private String memoContent;

}
