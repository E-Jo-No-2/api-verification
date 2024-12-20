package com.locationbase.controller;

import com.locationbase.entity.PlannerEntity;
import com.locationbase.service.PlannerService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/planner")
public class PlannerController {

    private static final Logger logger = LoggerFactory.getLogger(PlannerController.class);

    private final PlannerService plannerService;
    // 최신 플래너 ID 반환 메서드
    @Getter
    private static Integer latestPlannerId; // 최신 플래너 ID 저장

    @Autowired
    public PlannerController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> savePlanner(@RequestBody Map<String, String> payload) {
        String userId = payload.get("userId");
        String date = payload.get("date");

        try {
            // userId와 date 유효성 확인
            if (userId == null || userId.isEmpty()) {
                logger.error("userId가 유효하지 않습니다.");
                return ResponseEntity.badRequest().body("userId는 필수 값입니다.");
            }
            if (date == null || date.isEmpty()) {
                logger.error("date가 유효하지 않습니다.");
                return ResponseEntity.badRequest().body("date는 필수 값입니다.");
            }


            // date를 LocalDate로 변환
            LocalDate plannerDate = LocalDate.parse(date);

            // Planner 저장
            int generatedPlannerId = plannerService.savePlanner(userId, plannerDate);
            latestPlannerId = generatedPlannerId; // 최신 플래너 ID 저장
            logger.info("userId: {}, generated plannerId: {}, date: {}", userId, generatedPlannerId, plannerDate);

            // JSON 응답 반환
            return ResponseEntity.ok().body("{\"plannerId\": " + generatedPlannerId + ", \"date\": \"" + plannerDate + "\"}");
        } catch (RuntimeException e) {
            logger.error("플래너 저장 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(500).body("{\"error\": \"플래너 저장 실패: " + e.getMessage() + "\"}");
        }
    }

    @PutMapping("/update")
    public String updatePlanner(@RequestParam int plannerId,
                                @RequestParam String userId,
                                @RequestParam String newDate) {
        logger.info("Planner 업데이트 요청. Planner ID: {}, User ID: {}, 새 날짜: {}", plannerId, userId, newDate);

        try {
            // newDate를 LocalDate로 변환
            LocalDate parsedDate = LocalDate.parse(newDate);

            plannerService.updatePlanner(plannerId, userId, parsedDate);
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

    @GetMapping("/list")
    public ResponseEntity<?> getPlannerList(@RequestParam(required = true) String userId) {
        try {
            // PlannerService에서 플래너 목록 조회
            List<PlannerEntity> planners = plannerService.getPlannerListByUser(userId);
            return ResponseEntity.ok(planners);
        } catch (Exception e) {
            logger.error("Planner 목록 불러오기 실패: {}", e.getMessage());
            return ResponseEntity.status(500).body("Planner 목록 불러오기 실패");
        }
    }
}
