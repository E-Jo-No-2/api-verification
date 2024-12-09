package com.locationbase.controller;

import com.locationbase.service.PlannerSpotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/planner-spot")
public class PlannerSpotController {

    private final PlannerSpotService plannerSpotService;

    public PlannerSpotController(PlannerSpotService plannerSpotService) {
        this.plannerSpotService = plannerSpotService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> savePlannerSpot(@RequestBody Map<String, Object> requestData) {
        try {
            String spotName = (String) requestData.get("spot_name");
            int plannerId = (int) requestData.get("planner_id"); //planerId 저장

            // PlannerSpot 테이블 저장
            plannerSpotService.savePlannerSpot(spotName, plannerId );//plannerId 저장

            return ResponseEntity.ok("Planner spot saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save planner spot");
        }
    }
}

