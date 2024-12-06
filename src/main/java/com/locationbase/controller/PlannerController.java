package com.locationbase.controller;

import com.locationbase.entity.PlannerEntity;
import com.locationbase.service.MemoService;
import com.locationbase.service.PlannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/planner")
public class PlannerController {

    private static final Logger logger = LoggerFactory.getLogger(PlannerController.class);

    private final PlannerService plannerService;
    private final MemoService memoService;

    public PlannerController(PlannerService plannerService, MemoService memoService) {
        this.plannerService = plannerService;
        this.memoService = memoService;
    }

    // 플래너 저장
    @PostMapping("/save")
    public ResponseEntity<Object> savePlanner(@RequestBody PlannerEntity planner) {
        logger.debug("Save Planner endpoint 호출: User ID: {}, Date: {}", planner.getUser().getUserId(), planner.getDate());
        try {
            // 1. 플래너 저장
            PlannerEntity savedPlanner = plannerService.savePlanner(planner.getUser().getUserId(), planner.getDate());

            // 2. 해당 플래너의 기본 메모 생성
            memoService.createMemo("기본 메모 내용", savedPlanner.getPlannerId());

            return ResponseEntity.ok(savedPlanner);
        } catch (Exception e) {
            logger.error("플래너 저장 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("플래너 저장 중 오류 발생");
        }
    }

    // 플래너 수정
    @PutMapping("/update")
    public ResponseEntity<String> updatePlanner(
            @RequestParam int plannerId,
            @RequestParam String userId,
            @RequestParam String newDate) {
        logger.debug("Update Planner endpoint 호출: Planner ID: {}, User ID: {}, New Date: {}", plannerId, userId, newDate);
        try {
            plannerService.updatePlanner(plannerId, userId, LocalDate.parse(newDate));
            return ResponseEntity.ok("플래너가 성공적으로 수정되었습니다");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // 플래너 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePlanner(@RequestParam int plannerId) {
        logger.debug("Delete Planner endpoint 호출: Planner ID: {}", plannerId);
        try {
            plannerService.deletePlanner(plannerId);
            return ResponseEntity.ok("플래너가 성공적으로 삭제되었습니다");
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
