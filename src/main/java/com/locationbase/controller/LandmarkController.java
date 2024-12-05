package com.locationbase.controller;

import com.locationbase.entity.LandMarkEntity;
import com.locationbase.service.LandmarkService;
import com.locationbase.service.PlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LandmarkController {

    private final LandmarkService landmarkService;
    private final PlannerService plannerService;

    @Autowired
    public LandmarkController(LandmarkService landmarkService, PlannerService plannerService) {
        this.landmarkService = landmarkService;
        this.plannerService = plannerService;
    }

    // /landmark 요청이 들어오면 SelectLandmark.html 반환
    @GetMapping("/landmark")
    public String showSelectLandmark(@RequestParam("userId") String userId, @RequestParam("date") String date) {
        // Planner 테이블 업데이트
        LocalDate parsedDate = LocalDate.parse(date);
        plannerService.savePlanner(userId, parsedDate);

        // Service를 호출하여 비즈니스 로직 수행 (필요 시)
        String info = landmarkService.getLandmarkInfo();
        System.out.println("User ID: " + userId); // 디버깅용 로그
        System.out.println("Date: " + date); // 디버깅용 로그
        System.out.println(info); // 디버깅용 로그
        return "SelectLandmark"; // templates/SelectLandmark.html 반환
    }

    @GetMapping("/api/landmark/coordinates")
    @ResponseBody
    public ResponseEntity<Map<String, String>> getCoordinates(@RequestParam("landmarkName") String landmarkName) {
        LandMarkEntity landmark = landmarkService.findByName(landmarkName);
        if (landmark != null) {
            Map<String, String> coordinates = new HashMap<>();
            coordinates.put("latitude", landmark.getLatitude());
            coordinates.put("longitude", landmark.getLongitude());
            return ResponseEntity.ok(coordinates);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
