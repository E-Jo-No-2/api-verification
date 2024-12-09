package com.locationbase.dto;

import lombok.Data;

@Data
public class LandMarkDTO {
    private int landmark_id;   // 랜드마크의 고유 ID (Primary Key)
    private String landmark_name;   // 랜드마크의 이름
    private String longitude;   // 랜드마크의 경도
    private String latitude;   // 랜드마크의 위도
    private String cat1;   // 추가적인 카테고리 정보
    private String distance;   // 특정 기준점과의 거리
}
