package com.locationbase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class  RatingDTO {
    private double averageRating;
    private int reviewCount;
    private String placeName;
    private List<ReviewDTO> reviews;
}
