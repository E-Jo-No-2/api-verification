package com.locationbase.controller;

import com.locationbase.service.PlannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/planner")
public class PlannerController {

    private static final Logger logger = LoggerFactory.getLogger(PlannerController.class);

    private final PlannerService plannerService;

    @Autowired
    public PlannerController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    @PostMapping("/save")
    public String savePlanner(@RequestParam String userId) {
        try {
            // planner 저장 요청: plannerId는 서버에서 자동 생성됨
            int generatedPlannerId = plannerService.savePlanner(userId);

            // 정상적으로 plannerId가 생성되었으면, 랜드마크 페이지로 리디렉션
            // 리디렉션 경로를 문자열로 반환
            return "redirect:/landmark?plannerId=" + generatedPlannerId + "&userId=" + userId;

        } catch (RuntimeException e) {
            logger.error("플래너 저장 중 오류 발생: {}", e.getMessage());
            // 예외 발생 시 ResponseEntity를 사용해 에러 메시지를 반환
            return "플래너 저장 실패: " + e.getMessage();
        }
    }



    @PutMapping("/update")
    public String updatePlanner(@RequestParam int plannerId,
                                @RequestParam String userId,
                                @RequestParam LocalDate newDate) {
        logger.info("Planner 업데이트 요청. Planner ID: {}, User ID: {}, 새 날짜: {}", plannerId, userId, newDate);

        try {
            plannerService.updatePlanner(plannerId, userId, newDate);
            logger.info("Planner 업데이트 성공. Planner ID: {}", plannerId);
            return "Planner 업데이트 성공";
        } catch (RuntimeException e) {
            logger.error("Planner 업데이트 중 오류 발생. 오류 메시지: {}", e.getMessage());
            return "Planner 업데이트 실패: " + e.getMessage();
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePlanner(@RequestParam int plannerId) {
        logger.info("Planner 삭제 요청. Planner ID: {}", plannerId);

        try {
            plannerService.deletePlanner(plannerId);
            logger.info("Planner 삭제 성공. Planner ID: {}", plannerId);
            return ResponseEntity.ok("Planner 삭제 성공");
        } catch (RuntimeException e) {
            logger.error("Planner 삭제 중 오류 발생. 오류 메시지: {}", e.getMessage());
            return ResponseEntity.status(500).body("Planner 삭제 실패: " + e.getMessage());
        }
    }
}
