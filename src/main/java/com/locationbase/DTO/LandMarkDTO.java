package com.locationbase.DTO;

import lombok.Data;

@Data
public class LandMarkDTO {
    private int landmark_id;
    private String landmarkName;
    private String longitude;
    private String latitude;
}
