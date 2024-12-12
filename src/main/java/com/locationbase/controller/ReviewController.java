
// Controller 클래스
package com.locationbase.controller;

import com.locationbase.domain.repository.PlacesRepository;
import com.locationbase.dto.RatingDTO;
import com.locationbase.entity.PlacesEntity;
import com.locationbase.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/landmark")
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    private final ReviewService reviewService;
    private final PlacesRepository placeRepository;

    public ReviewController(ReviewService reviewService, PlacesRepository placeRepository) {
        this.reviewService = reviewService;
        this.placeRepository = placeRepository;
    }

    @GetMapping("/rating")
    public ResponseEntity<?> getRatingByLandmarkName(@RequestParam("name") String name) {
        logger.info("[INFO] getRatingByLandmarkName 호출됨 - name: {}", name);

        try {
            // 장소 이름으로 place 데이터 조회
            PlacesEntity place = placeRepository.findByName(name)
                    .orElseThrow(() -> new RuntimeException("장소를 찾을 수 없습니다: " + name));

            // 서비스에서 평점 및 리뷰 데이터 처리
            RatingDTO ratingData = reviewService.getRatingByPlaceId(place.getPlaceId(), place.getName());

            logger.info("[INFO] 응답 데이터 생성 성공 - response: {}", ratingData);
            return ResponseEntity.ok(ratingData);

        } catch (RuntimeException e) {
            logger.error("[ERROR] 장소를 찾을 수 없습니다: {}", name, e);
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
}