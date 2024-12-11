package com.locationbase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlacesDTO {
    private int placeId;       // 장소의 고유 ID (Primary Key)
    private String lat;         // 장소의 위도
    private String lng;         // 장소의 경도
    private String name;        // 장소의 이름
}
