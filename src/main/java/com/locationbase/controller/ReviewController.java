package com.locationbase.controller;

import com.locationbase.domain.repository.PlaceRepository;
import com.locationbase.entity.PlacesEntity;
import com.locationbase.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/landmark")
public class ReviewController {

    private final ReviewService reviewService;
    private final PlaceRepository placeRepository;

    public ReviewController(ReviewService reviewService, PlaceRepository placeRepository) {
        this.reviewService = reviewService;
        this.placeRepository = placeRepository;
    }

    @GetMapping("/rating")
    public ResponseEntity<Map<String, Object>> getRatingByLandmarkName(@RequestParam String name) {
        // 1. 장소 이름으로 placeId 조회
        PlacesEntity place = placeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Place not found: " + name));

        // 2. placeId로 리뷰 데이터 조회 및 평점 계산
        Map<String, Object> ratingData = reviewService.getRatingByPlaceId(place.getPlaceId());
        ratingData.put("placeName", place.getName());

        return ResponseEntity.ok(ratingData);
    }
}
