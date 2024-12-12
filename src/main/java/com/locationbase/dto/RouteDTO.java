package com.locationbase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RouteDTO {
    private int routeId;           // 경로의 고유 ID (Primary Key)
    private int startPoint;        // 시작 지점의 ID (Place의 ID를 참조)
    private Integer endPoint;      // 종료 지점의 ID (Place의 ID를 참조, nullable)
    private String themeName;      // 테마 이름
    private Integer taxiFare;      // 택시 요금 (nullable)
    private String distance;       // 거리
    private Integer estimatedTime; // 예상 시간 (nullable)
    private Integer plannerId;     // 플래너의 ID (nullable)
}
