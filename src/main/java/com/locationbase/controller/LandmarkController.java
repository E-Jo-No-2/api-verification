package com.locationbase.controller;

import com.locationbase.service.LandmarkService;
import com.locationbase.service.PlannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class LandmarkController {

    private final PlannerService plannerService;
    private final LandmarkService landmarkService;

    @Autowired
    public LandmarkController(PlannerService plannerService, LandmarkService landmarkService) {
        this.plannerService = plannerService;
        this.landmarkService = landmarkService;
    }

    // /landmark 요청이 들어오면 SelectLandmark.html 반환
    @GetMapping("/landmark")
    public String showSelectLandmark(@RequestParam("plannerId") int plannerId, @RequestParam("userId") String userId) {
        System.out.println("플래너 ID: " + plannerId); // 디버깅용 로그
        return "SelectLandmark"; // templates/SelectLandmark.html 반환
    }

    // /api/landmark/coordinates 요청을 처리하는 엔드포인트 추가
    @GetMapping("/api/landmark/coordinates")
    public ResponseEntity<Map<String, String>> getLandmarkCoordinates(@RequestParam("landmarkName") String landmarkName) {
        System.out.println("요청된 랜드마크 이름: " + landmarkName); // 디버깅용 로그

        // 랜드마크 좌표 조회
        Map<String, String> coordinates = landmarkService.getLandmarkCoordinates(landmarkName);

        if (coordinates == null) {
            System.out.println("좌표를 찾을 수 없음: " + landmarkName); // 디버깅용 로그
            return ResponseEntity.notFound().build();
        }

        System.out.println("좌표 찾음 " + landmarkName + ": " + coordinates); // 디버깅용 로그
        return ResponseEntity.ok(coordinates);
    }
}
