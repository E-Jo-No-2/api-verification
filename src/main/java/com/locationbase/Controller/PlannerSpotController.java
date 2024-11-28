package com.locationbase.Controller;

import com.locationbase.Service.PlannerSpotService;
import com.locationbase.Entity.PlannerSpotEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mytourlists")
public class PlannerSpotController {

    private final PlannerSpotService plannerSpotService;

    public PlannerSpotController(PlannerSpotService plannerSpotService) {
        this.plannerSpotService = plannerSpotService;
    }


    @GetMapping("/planner/{plannerId}")
    public ResponseEntity<List<PlannerSpotEntity>> getSpotsForPlanner(@PathVariable Integer plannerId) {
        List<PlannerSpotEntity> spots = plannerSpotService.getSpotsForPlanner(plannerId);
        if (spots.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(spots, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<PlannerSpotEntity> addSpot(@RequestBody PlannerSpotEntity plannerSpotEntity) {
        PlannerSpotEntity newSpot = plannerSpotService.addSpot(plannerSpotEntity);
        return new ResponseEntity<>(newSpot, HttpStatus.CREATED);
    }


    @DeleteMapping("/{plannerSpotId}")
    public ResponseEntity<Void> removeSpot(@PathVariable Integer plannerSpotId) {
        plannerSpotService.removeSpot(plannerSpotId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
