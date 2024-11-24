package com.locationbase.DTO;

import lombok.Data;

@Data
public class LandMarkDTO {
    private int landmark_id;
    private String landmark_name;
    private String theme_name;
    private double longitude;
    private double latitude;
}
