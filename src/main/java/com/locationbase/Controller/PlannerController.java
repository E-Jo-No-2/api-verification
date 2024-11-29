package com.locationbase.Controller;

import com.locationbase.Service.PlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planner")
public class PlannerController {

    private final PlannerService plannerService;

    @Autowired
    public PlannerController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    // 플래너 저장 엔드포인트
    @PostMapping("/save")
    public ResponseEntity<String> savePlanner(
            @RequestParam int plannerId,
            @RequestParam String userId) {
        try {
            plannerService.savePlanner(plannerId, userId);
            return ResponseEntity.ok("Planner saved successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
