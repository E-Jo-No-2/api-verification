package com.locationbase.dto;

import lombok.Data;
import java.util.Date;

@Data
public class WeatherDTO {

    private Date date;         // 날씨 데이터가 기록된 날짜
    private String weather;    // 날씨 정보
    private String iconCode;   // 날씨 아이콘 코드
    private String iconUrl;    // 날씨 아이콘 URL
}
