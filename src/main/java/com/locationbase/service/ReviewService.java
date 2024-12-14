package com.locationbase.service;

import com.locationbase.Domain.repository.ReviewRepository;
import com.locationbase.dto.RatingDTO;
import com.locationbase.dto.ReviewDTO;
import com.locationbase.entity.ReviewEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public RatingDTO getRatingByPlaceId(int placeId, String placeName) {
        // 엔티티 리스트 조회
        List<ReviewEntity> reviewEntities = reviewRepository.findByPlaceId(placeId);

        // 엔티티 리스트를 DTO 리스트로 변환
        List<ReviewDTO> reviews = reviewEntities.stream()
                .map(entity -> new ReviewDTO(
                        entity.getReviewId(),
                        entity.getUser(),
                        entity.getRating(),
                        entity.getComment(),
                        entity.getCreateTime(),
                        entity.getPlace().getName()
                ))
                .collect(Collectors.toList());

        // 평균 평점 및 리뷰 수 계산
        double averageRating = reviews.stream()
                .mapToDouble(ReviewDTO::getRating)
                .average()
                .orElse(0.0);

        int reviewCount = reviews.size();

        return new RatingDTO(averageRating, reviewCount, placeName, reviews);
    }
}
