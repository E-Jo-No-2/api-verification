package com.locationbase.service;

import com.locationbase.domain.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // 장소별 평점 데이터 반환
    public Map<String, Object> getRatingByPlaceId(int placeId) {
        Object[] result = reviewRepository.getAverageRatingByPlaceId(placeId);

        // 결과를 Map으로 변환
        Map<String, Object> ratingData = new HashMap<>();
        ratingData.put("averageRating", result[0] != null ? ((Number) result[0]).doubleValue() : 0.0);
        ratingData.put("reviewCount", result[1] != null ? ((Number) result[1]).intValue() : 0);

        return ratingData;
    }
}
