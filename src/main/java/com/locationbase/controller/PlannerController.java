package com.locationbase.controller;

import com.locationbase.service.PlannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/planner")
public class PlannerController {

    private static final Logger logger = LoggerFactory.getLogger(PlannerController.class);

    private final PlannerService plannerService;

    @Autowired
    public PlannerController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    // Save Planner
    @PostMapping("/save")
    public ResponseEntity<String> savePlanner(
            @RequestParam String userId,
            @RequestParam String date) {
        logger.debug("Save Planner endpoint called with User ID: {}, Date: {}", userId, date);
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            plannerService.savePlanner(userId, parsedDate);
            return ResponseEntity.ok("Planner saved successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Update Planner
    @PutMapping("/update")
    public ResponseEntity<String> updatePlanner(
            @RequestParam int plannerId,
            @RequestParam String userId,
            @RequestParam String newDate) {
        logger.debug("Update Planner endpoint called with Planner ID: {}, User ID: {}, New Date: {}", plannerId, userId, newDate);
        try {
            LocalDate parsedDate = LocalDate.parse(newDate);
            plannerService.updatePlanner(plannerId, userId, parsedDate);
            return ResponseEntity.ok("Planner updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // Delete Planner
    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePlanner(@RequestParam int plannerId) {
        logger.debug("Delete Planner endpoint called with Planner ID: {}", plannerId);
        try {
            plannerService.deletePlanner(plannerId);
            return ResponseEntity.ok("Planner deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
