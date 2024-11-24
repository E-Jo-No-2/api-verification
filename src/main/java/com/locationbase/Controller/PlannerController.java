package com.locationbase.Controller;

import com.locationbase.DTO.PlannerDTO;
import com.locationbase.Service.PlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/planners")
public class PlannerController {

    @Autowired
    private PlannerService plannerService;

    // Create a new Planner with a theme and landmark
    @PostMapping
    public PlannerDTO createPlanner(@RequestBody PlannerDTO plannerDTO) {
        return plannerService.createPlanner(plannerDTO);
    }

    // Update an existing Planner
    @PutMapping("/{planner_id}")
    public PlannerDTO updatePlanner(@PathVariable int planner_id, @RequestBody PlannerDTO plannerDTO) {
        return plannerService.updatePlanner(planner_id, plannerDTO);
    }

    // Delete a Planner
    @DeleteMapping("/{planner_id}")
    public void deletePlanner(@PathVariable int planner_id) {
        plannerService.deletePlanner(planner_id);
    }
}
