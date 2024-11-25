package com.locationbase.Controller;

import Domain.Entity.LandMarkEntity;
import Domain.Entity.PlannerEntity;
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

    // Create a new planner
    @PostMapping("/create")
    public ResponseEntity<PlannerEntity> createPlanner(
            @RequestParam Date start_date,
            @RequestParam String theme_name) {
        PlannerEntity planner = plannerService.createPlanner(start_date, theme_name);
        return ResponseEntity.ok(planner);
    }

    // Retrieve landmarks by theme
    @GetMapping("/landmarks")
    public ResponseEntity<List<LandMarkEntity>> getLandmarksByTheme(@RequestParam String theme_name) {
        List<LandMarkEntity> landmarks = plannerService.getLandmarksByTheme(theme_name);
        return ResponseEntity.ok(landmarks);
    }

    // Update an existing planner
    @PutMapping("/{plannerId}")
    public ResponseEntity<PlannerEntity> updatePlanner(
            @PathVariable Integer plannerId,
            @RequestParam Date newStart_date,
            @RequestParam String newTheme_name) {
        PlannerEntity updatedPlanner = plannerService.updatePlanner(plannerId, newStart_date, newTheme_name);
        return ResponseEntity.ok(updatedPlanner);
    }

    // Delete a planner
    @DeleteMapping("/{plannerId}")
    public ResponseEntity<Void> deletePlanner(@PathVariable Integer plannerId) {
        plannerService.deletePlanner(plannerId);
        return ResponseEntity.noContent().build();
    }
}
