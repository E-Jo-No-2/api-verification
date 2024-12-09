package com.locationbase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class PlannerDTO {
    private int plannerId;  // 플래너의 고유 ID (Primary Key)
    private String userId;  // 관련된 사용자의 ID (Foreign Key)
    private Date date;      // 플래너의 날짜
}
