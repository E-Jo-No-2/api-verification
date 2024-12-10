package com.locationbase.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewDTO {
    private int reviewId;          // 리뷰의 고유 ID (Primary Key)
    private String userId;         // 작성자의 사용자 ID (Foreign Key)
    private double rating;         // 리뷰의 평점
    private String comment;        // 리뷰 내용
    private LocalDateTime createTime;  // 리뷰 작성 시간
    private String spotName;       // 리뷰한 장소의 이름
}
