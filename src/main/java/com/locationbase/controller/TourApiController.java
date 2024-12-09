package com.locationbase.controller;

import com.locationbase.dto.LandmarkDTO;
import com.locationbase.service.TourApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tour")
public class TourApiController {

    private static final Logger logger = LoggerFactory.getLogger(TourApiController.class);
    private final TourApiService tourApiService;

    public TourApiController(TourApiService tourApiService) {
        this.tourApiService = tourApiService;
    }

    @GetMapping("/nearby")
    public Map<String, List<LandmarkDTO>> getNearbyLandmarks(
            @RequestParam(name = "longitude") String longitude,
            @RequestParam(name = "latitude") String latitude
    ) {
        logger.info("주변 관광지 요청 수신");
        logger.info("경도: {}, 위도: {}", longitude, latitude);

        try {
            Map<String, List<LandmarkDTO>> landmarks = tourApiService.getNearbySpotsByTheme(longitude, latitude);
            logger.info("응답 데이터에 포함된 테마 수: {}", landmarks.size());
            landmarks.forEach((theme, spots) ->
                    logger.info("테마: {}, 관광지 개수: {}", theme, spots.size())
            );
            return landmarks;
        } catch (Exception e) {
            logger.error("주변 관광지 데이터를 가져오는 중 오류 발생: ", e);
            throw e;
        }
    }
}
