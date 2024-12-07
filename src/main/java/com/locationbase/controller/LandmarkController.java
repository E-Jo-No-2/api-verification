package com.locationbase.controller;

import com.locationbase.service.PlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LandmarkController {

    private final PlannerService plannerService;

    @Autowired
    public LandmarkController(PlannerService plannerService) {
        this.plannerService = plannerService;
    }

    // /landmark 요청이 들어오면 SelectLandmark.html 반환
    @GetMapping("/landmark")
    public String showSelectLandmark(@RequestParam("plannerId") int plannerId, @RequestParam("userId") String userId) {
        // Planner 테이블 업데이트


        // Service를 호출하여 비즈니스 로직 수행 (필요 시)
        System.out.println("Planner ID: " + plannerId); // 디버깅용 로그

        return "SelectLandmark"; // templates/SelectLandmark.html 반환
    }
}
