
// Service 클래스
package com.locationbase.service;

import com.locationbase.domain.repository.ReviewRepository;
import com.locationbase.dto.ReviewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // 장소별 평점 데이터 반환
    public Map<String, Object> getRatingByPlaceId(int placeId) {
        logger.info("[INFO] getRatingByPlaceId 호출됨 - placeId: {}", placeId);

        // 리뷰 데이터 조회
        List<ReviewDTO> reviews = reviewRepository.getReviewsByPlaceId(placeId);
        logger.info("[INFO] DB에서 조회된 리뷰 데이터: {}", reviews);

        // 평균 평점 및 리뷰 수 계산
        double averageRating = reviews.stream()
                .mapToDouble(ReviewDTO::getRating)
                .average()
                .orElse(0.0);
        logger.info("[INFO] 평균 평점 계산 결과: {}", averageRating);

        int reviewCount = reviews.size();
        logger.info("[INFO] 리뷰 개수: {}", reviewCount);

        // 결과 데이터 구성
        Map<String, Object> ratingData = new HashMap<>();
        ratingData.put("averageRating", averageRating);
        ratingData.put("reviewCount", reviewCount);
        ratingData.put("reviews", reviews);

        logger.info("[INFO] 반환할 데이터: {}", ratingData);
        return ratingData;
    }
}
