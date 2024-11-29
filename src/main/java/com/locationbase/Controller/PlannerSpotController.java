package com.locationbase.Controller;

import com.locationbase.Entity.PlannerSpotEntity;
import com.locationbase.Service.PlannerSpotService;
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
}


