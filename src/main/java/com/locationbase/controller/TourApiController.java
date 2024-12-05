package com.locationbase.controller;

import com.locationbase.dto.LandMarkDTO;
import com.locationbase.service.TourApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tour")
public class TourApiController {

    // 로거 생성
    private static final Logger logger = LoggerFactory.getLogger(TourApiController.class);

    // TourApiService 주입
    private final TourApiService tourApiService;

    public TourApiController(TourApiService tourApiService) {
        this.tourApiService = tourApiService;
    }

    /**
     * 사용자 주변 관광지 정보를 가져오는 엔드포인트
     * @param latitude 위도
     * @param longitude 경도
     * @return 테마별로 그룹화된 관광지 목록
     */
    @GetMapping("/nearby")
    public Map<String, List<LandMarkDTO>> getNearbyLandmarks(
            @RequestParam(name = "longitude") String longitude,
            @RequestParam(name = "latitude") String latitude
    )
    {
        logger.info("주변 관광지 요청 수신");
        logger.info("경도: {}, 위도: {},", longitude,latitude);

        try {
            // TourApiService를 통해 관광지 데이터 가져오기
            Map<String, List<LandMarkDTO>> landmarks = tourApiService.getNearbySpotsByTheme(longitude,latitude);

            logger.info("응답 데이터에 포함된 테마 수: {}", landmarks.size());
            landmarks.forEach((theme, spots) ->
                    logger.info("테마: {}, 관광지 개수: {}", theme, spots.size())
            );

            return landmarks;
        } catch (Exception e) {
            // 에러 발생 시 로깅
            logger.error("주변 관광지 데이터를 가져오는 중 오류 발생: ", e);
            throw e; // 적절한 에러 응답 처리 추가 가능
        }
    }
    // 추가한 부분
    @PostMapping("/save")
    public ResponseEntity<String> saveSelectedSpot(@RequestBody Map<String, Object> requestData) {
        try {
            String spotName = (String) requestData.get("spot_name");

            // PlannerSpot 저장
            tourApiService.savePlannerSpot(spotName);

            return ResponseEntity.ok("Spot saved successfully");
        } catch (Exception e) {
            e.printStackTrace(); // 예외 로깅
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save location");
        }
    }
}