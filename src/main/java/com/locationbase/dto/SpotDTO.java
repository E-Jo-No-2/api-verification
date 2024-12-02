package com.locationbase.dto;

import lombok.Data;

@Data
public class SpotDTO {

    private Integer spot_id;
    private String spot_name;
    private Double longitude;
    private Double latitude;

}

