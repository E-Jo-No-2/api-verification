package com.locationbase.service;

import com.locationbase.domain.repository.ReviewRepository;
import com.locationbase.dto.ReviewDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // 장소별 평점 데이터 반환
    public Map<String, Object> getRatingByPlaceId(int placeId) {
        List<ReviewDTO> reviews = reviewRepository.getReviewsByPlaceId(placeId);

        // 평균 평점 및 리뷰 수 계산
        double averageRating = reviews.stream()
                .mapToDouble(ReviewDTO::getRating)
                .average()
                .orElse(0.0);
        int reviewCount = reviews.size();

        // 결과 데이터 구성
        Map<String, Object> ratingData = new HashMap<>();
        ratingData.put("averageRating", averageRating);
        ratingData.put("reviewCount", reviewCount);
        ratingData.put("reviews", reviews);

        return ratingData;
    }
}
