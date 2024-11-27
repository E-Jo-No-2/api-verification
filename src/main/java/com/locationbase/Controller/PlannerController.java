
package com.locationbase.Controller;


import com.locationbase.DTO.PlannerDTO;
import com.locationbase.Entity.LandMarkEntity;
import com.locationbase.Entity.PlannerEntity;
import com.locationbase.Service.PlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/planner")
public class PlannerController {

    @Autowired
    private PlannerService plannerService;


    @PostMapping("/create")
    public ResponseEntity<PlannerEntity> createPlanner(@RequestBody PlannerDTO plannerDTO) {
        PlannerEntity planner = plannerService.createPlanner(plannerDTO);
        return ResponseEntity.ok(planner);
    }


    @GetMapping("/recommendations/{date}")
    public ResponseEntity<List<LandMarkEntity>> recommend(@PathVariable String date) {
        List<LandMarkEntity> recommendations = plannerService.recommendByWeather(LocalDate.parse(date));
        return ResponseEntity.ok(recommendations);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<PlannerEntity> updatePlanner(@PathVariable int planner_id, @RequestBody PlannerDTO plannerDTO) {
        PlannerEntity updatedPlanner = plannerService.updatePlanner(planner_id, plannerDTO);
        return ResponseEntity.ok(updatedPlanner);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlanner(@PathVariable int planner_id) {
        plannerService.deletePlanner(planner_id);
        return ResponseEntity.ok("Planner deleted successfully");
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlannerEntity>> getPlannersByUser(@PathVariable String user_id) {
        List<PlannerEntity> planners = plannerService.getPlannersByUser(user_id);
        return ResponseEntity.ok(planners);
    }
}
