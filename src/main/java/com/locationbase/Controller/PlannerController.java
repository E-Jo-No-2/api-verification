/*
package com.locationbase.Controller;

import com.locationbase.Entity.LandMarkEntity;
import com.locationbase.Entity.PlannerEntity;
import com.locationbase.Service.PlannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/planners")
public class PlannerController {

    private final PlannerService plannerService;

    public PlannerController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }


    @PostMapping("/create")
    public ResponseEntity<PlannerEntity> createPlanner(
            @RequestParam Date start_date,
            @RequestParam String theme_name) {
        PlannerEntity planner = plannerService.createPlanner(start_date, theme_name);
        return ResponseEntity.ok(planner);
    }


    @GetMapping("/landmarks")
    public ResponseEntity<List<LandMarkEntity>> getLandmarksByTheme(@RequestParam String theme_name) {
        List<LandMarkEntity> landmarks = plannerService.getLandmarksByTheme(theme_name);
        return ResponseEntity.ok(landmarks);
    }


    @PutMapping("/{planner_id}")
    public ResponseEntity<PlannerEntity> updatePlanner(
            @PathVariable Integer planner_id,
            @RequestParam Date newStart_date,
            @RequestParam String newTheme_name) {
        PlannerEntity updatedPlanner = plannerService.updatePlanner(planner_id, newStart_date, newTheme_name);
        return ResponseEntity.ok(updatedPlanner);
    }


    @DeleteMapping("/{planner_id}")
    public ResponseEntity<Void> deletePlanner(@PathVariable Integer planner_id) {
        plannerService.deletePlanner(planner_id);
        return ResponseEntity.noContent().build();
    }
}
*/