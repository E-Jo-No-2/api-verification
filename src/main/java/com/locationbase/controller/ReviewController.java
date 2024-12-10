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

    @GetMapping("/rating")
    public ResponseEntity<Map<String, Object>> getRatingByLandmarkName(@RequestParam("name") String name) {
        try {
            // 1. 이름으로 placeId 조회
            PlacesEntity place = placeRepository.findByName(name)
                    .orElseThrow(() -> new RuntimeException("장소를 찾을 수 없습니다: " + name));

            // 2. placeId로 리뷰 데이터 조회
            Map<String, Object> ratingData = reviewService.getRatingByPlaceId(place.getPlaceId());
            ratingData.put("placeName", place.getName());

            return ResponseEntity.ok(ratingData);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "장소를 찾을 수 없습니다: " + name
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", "내부 서버 오류: " + e.getMessage()
            ));
        }
    }
}