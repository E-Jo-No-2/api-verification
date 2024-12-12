package com.locationbase.dto;

import lombok.Data;

@Data
public class PlannerSpotDTO {
    private int plannerSpotId;  // 플래너 스팟의 고유 ID (Primary Key)
    private int plannerId;      // 관련된 플래너의 ID (Foreign Key)
    private String spotName;    // 스팟의 이름
    private int visitOrder;     // 방문 순서
    private int routeId;        // 관련된 경로의 ID (Foreign Key)
}
