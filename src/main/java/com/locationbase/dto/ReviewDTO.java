package com.locationbase.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {

    private int review_id;
    private String user_id;
    private double rating;
    private String comment;
    private LocalDateTime create_time;
    private String longitude;
    private String latitude;
    private String spot_name;


}
