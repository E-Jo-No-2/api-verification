package com.locationbase.controller;
/*
import com.locationbase.entity.PlannerSpotEntity;
import com.locationbase.service.PlannerSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/plannerSpot")
public class PlannerSpotController {

    private final PlannerSpotService plannerSpotService;

    @Autowired
    public PlannerSpotController(PlannerSpotService plannerSpotService) {
        this.plannerSpotService = plannerSpotService;
    }

    @GetMapping("/getSpots/{plannerId}")
    public ResponseEntity<List<PlannerSpotEntity>> getSpotsByPlanner(@PathVariable int plannerId) {
        List<PlannerSpotEntity> spots = plannerSpotService.getSpotsByPlanner(plannerId);
        return ResponseEntity.ok(spots);
    }


    @GetMapping("/getSpot/{plannerSpotId}")
    public ResponseEntity<PlannerSpotEntity> getSpotById(@PathVariable int plannerSpotId) {
        PlannerSpotEntity spot = plannerSpotService.getSpotById(plannerSpotId);
        return ResponseEntity.ok(spot);
    }


    @PostMapping("/addSpot")
    public ResponseEntity<PlannerSpotEntity> addSpot(@RequestParam int plannerId,
                                                     @RequestParam String spotName,
                                                     @RequestParam int visitOrder,
                                                     @RequestParam int routeId) {
        PlannerSpotEntity newSpot = plannerSpotService.addSpot(plannerId, spotName, visitOrder, routeId);
        return ResponseEntity.ok(newSpot);
    }

    @PutMapping("/updateSpot/{plannerSpotId}")
    public ResponseEntity<PlannerSpotEntity> updateSpot(@PathVariable int plannerSpotId,
                                                        @RequestParam String spotName,
                                                        @RequestParam int visitOrder,
                                                        @RequestParam int routeId) {
        PlannerSpotEntity updatedSpot = plannerSpotService.updateSpot(plannerSpotId, spotName, visitOrder, routeId);
        return ResponseEntity.ok(updatedSpot);
    }


    @DeleteMapping("/deleteSpot/{plannerSpotId}")
    public ResponseEntity<String> deleteSpot(@PathVariable int plannerSpotId) {
        plannerSpotService.deleteSpot(plannerSpotId);
        return ResponseEntity.ok("Spot deleted successfully");
    }
}*/

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

            // PlannerSpot 테이블 저장
            plannerSpotService.savePlannerSpot(spotName);

            return ResponseEntity.ok("Planner spot saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save planner spot");
        }
    }
}

