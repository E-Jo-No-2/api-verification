
// Controller 클래스
package com.locationbase.controller;

import com.locationbase.domain.repository.PlacesRepository;
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
    public ResponseEntity<Map<String, Object>> getRatingByLandmarkName(@RequestParam("name") String name) {
        logger.info("[INFO] getRatingByLandmarkName 호출됨 - name: {}", name);

        try {
            // 1. 이름으로 placeId 조회
            logger.info("[INFO] placeRepository.findByName 호출 - 요청 이름: {}", name);
            PlacesEntity place = placeRepository.findByName(name)
                    .orElseThrow(() -> new RuntimeException("장소를 찾을 수 없습니다: " + name));
            logger.info("[INFO] DB에서 조회된 장소 데이터: {}", place);

            // 2. placeId로 리뷰 데이터 조회
            logger.info("[INFO] reviewService.getRatingByPlaceId 호출 - placeId: {}", place.getPlaceId());
            Map<String, Object> ratingData = reviewService.getRatingByPlaceId(place.getPlaceId());
            logger.info("[INFO] 리뷰 데이터 조회 성공 - ratingData: {}", ratingData);

            ratingData.put("placeName", place.getName());
            logger.info("[INFO] 반환 데이터 구성 완료 - 최종 데이터: {}", ratingData);

            return ResponseEntity.ok(ratingData);
        } catch (RuntimeException e) {
            logger.error("[ERROR] 장소를 찾을 수 없습니다: {}", name, e);
            return ResponseEntity.status(404).body(Map.of(
                    "error", "장소를 찾을 수 없습니다: " + name
            ));
        } catch (Exception e) {
            logger.error("[ERROR] 내부 서버 오류 발생", e);
            return ResponseEntity.status(500).body(Map.of(
                    "error", "내부 서버 오류: " + e.getMessage()
            ));
        }
    }
}
