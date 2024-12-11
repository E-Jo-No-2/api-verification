package com.locationbase.controller;

import com.locationbase.entity.PlannerEntity;
import com.locationbase.service.PlannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    public ResponseEntity<?> savePlanner(@RequestParam(required = true) String userId) {
        try {
            // userId가 유효한지 확인
            if (userId == null || userId.isEmpty()) {
                logger.error("userId가 유효하지 않습니다.");
                return ResponseEntity.badRequest().body("userId는 필수 값입니다.");
            }

            // planner 저장 요청(user테이블에 있는 userId를 불러와 planner에 저장)
            int generatedPlannerId = plannerService.savePlanner(userId);
            logger.info("userId: " + userId + ", generated plannerId: " + generatedPlannerId);

            // JSON 응답 반환
            return ResponseEntity.ok().body("{\"plannerId\": " + generatedPlannerId + "}");
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
