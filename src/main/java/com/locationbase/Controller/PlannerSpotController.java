/*package com.locationbase.Controller;

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
}*/

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

    // Get all spots for a given planner
    @GetMapping("/getSpots/{plannerId}")
    public ResponseEntity<List<PlannerSpotEntity>> getSpotsByPlanner(@PathVariable int plannerId) {
        List<PlannerSpotEntity> spots = plannerSpotService.getSpotsByPlanner(plannerId);
        return ResponseEntity.ok(spots);
    }

    // Get a specific spot by its ID
    @GetMapping("/getSpot/{plannerSpotId}")
    public ResponseEntity<PlannerSpotEntity> getSpotById(@PathVariable int plannerSpotId) {
        PlannerSpotEntity spot = plannerSpotService.getSpotById(plannerSpotId);
        return ResponseEntity.ok(spot);
    }

    // Add a new spot to the planner
    @PostMapping("/addSpot")
    public ResponseEntity<PlannerSpotEntity> addSpot(@RequestParam int plannerId,
                                                     @RequestParam String spotName,
                                                     @RequestParam int visitOrder,
                                                     @RequestParam int routeId) {
        PlannerSpotEntity newSpot = plannerSpotService.addSpot(plannerId, spotName, visitOrder, routeId);
        return ResponseEntity.ok(newSpot);
    }

    // Update an existing spot
    @PutMapping("/updateSpot/{plannerSpotId}")
    public ResponseEntity<PlannerSpotEntity> updateSpot(@PathVariable int plannerSpotId,
                                                        @RequestParam String spotName,
                                                        @RequestParam int visitOrder,
                                                        @RequestParam int routeId) {
        PlannerSpotEntity updatedSpot = plannerSpotService.updateSpot(plannerSpotId, spotName, visitOrder, routeId);
        return ResponseEntity.ok(updatedSpot);
    }

    // Delete a spot
    @DeleteMapping("/deleteSpot/{plannerSpotId}")
    public ResponseEntity<String> deleteSpot(@PathVariable int plannerSpotId) {
        plannerSpotService.deleteSpot(plannerSpotId);
        return ResponseEntity.ok("Spot deleted successfully");
    }
}


