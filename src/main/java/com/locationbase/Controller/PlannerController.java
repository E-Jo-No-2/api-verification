
/*package com.locationbase.Controller;


import com.locationbase.DTO.PlannerDTO;
import com.locationbase.Entity.LandMarkEntity;
import com.locationbase.Entity.PlannerEntity;
import com.locationbase.Service.PlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
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
    public ResponseEntity<?> recommendByWeather(@PathVariable String date) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            List<LandMarkEntity> recommendations = plannerService.recommendByWeather(parsedDate);

            if (recommendations.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(recommendations);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Please use YYYY-MM-DD.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
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



    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<PlannerEntity>> getPlannersByUser(@PathVariable String user_id) {
        List<PlannerEntity> planners = plannerService.getPlannersByUser(String.valueOf(Integer.parseInt(user_id)));
        return ResponseEntity.ok(planners);
    }
}*/
