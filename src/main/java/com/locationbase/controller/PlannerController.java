package com.locationbase.controller;


import com.locationbase.entity.WeatherEntity;
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
    public String savePlanner(@RequestParam int plannerId, @RequestParam String userId) {
        logger.info("Planner 저장 요청. Planner ID: {}, User ID: {}", plannerId, userId);

        try {
            plannerService.savePlanner(plannerId, userId);
            return "Planner 저장 성공";
        } catch (RuntimeException e) {
            logger.error("Planner 저장 중 오류 발생: {}", e.getMessage());
            return "Planner 저장 실패: " + e.getMessage();
        }
    }


    @PutMapping("/update")
    public String updatePlanner(@RequestParam int plannerId,
                                @RequestParam String userId,
                                @RequestParam LocalDate newDate) {
        logger.info("Planner 업데이트 요청. Planner ID: {}, User ID: {}", plannerId, userId);

        try {
            plannerService.updatePlanner(plannerId, userId, newDate);
            return "Planner 업데이트 성공";
        } catch (RuntimeException e) {
            logger.error("Planner 업데이트 중 오류 발생: {}", e.getMessage());
            return "Planner 업데이트 실패: " + e.getMessage();
        }
    }


    @DeleteMapping("/delete")
    public String deletePlanner(@RequestParam  int plannerId) {
        logger.info("Planner 삭제 요청. Planner ID: {}", plannerId);

        try {
            plannerService.deletePlanner(plannerId);
            return "Planner 삭제 성공";
        } catch (RuntimeException e) {
            logger.error("Planner 삭제 중 오류 발생: {}", e.getMessage());
            return "Planner 삭제 실패: " + e.getMessage();
        }
    }
    @DeleteMapping("/delete-and-reset")
    public ResponseEntity<String> deletePlannerAndResetAutoIncrement(@RequestParam int plannerId) {
        logger.debug("Planner 삭제 및 AUTO_INCREMENT 초기화 요청. Planner ID: {}", plannerId);

        try {
            plannerService.deletePlannerAndResetAutoIncrement(plannerId);
            logger.debug("Planner 삭제 및 AUTO_INCREMENT 초기화 성공. Planner ID: {}", plannerId);
            return ResponseEntity.ok("Planner 삭제 및 AUTO_INCREMENT 초기화 성공");
        } catch (RuntimeException e) {
            logger.error("Planner 삭제 및 AUTO_INCREMENT 초기화 실패", e);
            return ResponseEntity.badRequest().body("Planner 삭제 및 AUTO_INCREMENT 초기화 실패: " + e.getMessage());
        }
    }


}
