package com.locationbase.controller;

import com.locationbase.service.PlannerSpotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
@RequestMapping("/api/planner-spot")
public class PlannerSpotController {

    private static final Logger logger = LoggerFactory.getLogger(PlannerSpotController.class);

    private final PlannerSpotService plannerSpotService;

    public PlannerSpotController(PlannerSpotService plannerSpotService) {
        this.plannerSpotService = plannerSpotService;
    }

    /**
     * 새로운 PlannerSpot을 저장합니다.
     * @param requestData 요청 데이터 (spot_name 포함)
     * @return 성공 또는 실패 메시지
     */
    @PostMapping("/save")
    public ResponseEntity<String> savePlannerSpot(@RequestBody Map<String, Object> requestData) {
        try {
            String spotName = (String) requestData.get("spot_name");

            // PlannerSpot 테이블 저장
            plannerSpotService.savePlannerSpot(spotName);

            return ResponseEntity.ok("플래너 스팟이 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            logger.error("플래너 스팟 저장 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("플래너 스팟 저장에 실패하였습니다.");
        }
    }
}
