package com.locationbase.controller;

import com.locationbase.domain.repository.PlacesRepository;
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
    private final PlacesRepository placeRepository;

    public ReviewController(ReviewService reviewService, PlacesRepository placeRepository) {
        this.reviewService = reviewService;
        this.placeRepository = placeRepository;
    }

    // 특정 장소의 평점 데이터 반환
    @GetMapping("/rating")
    public ResponseEntity<Map<String, Object>> getRatingByLandmarkName(@RequestParam String name) {
        // 1. 장소 이름으로 placeId 조회
        PlacesEntity place = (PlacesEntity) placeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Place not found: " + name));

        // 2. placeId를 통해 리뷰 데이터 조회 및 평점 계산
        Map<String, Object> ratingData = reviewService.getRatingByPlaceId(place.getPlaceId());
        ratingData.put("placeName", place.getName()); // 장소 이름 추가

        return ResponseEntity.ok(ratingData);
    }
}
