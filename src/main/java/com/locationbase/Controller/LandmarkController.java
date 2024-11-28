package com.locationbase.Controller;

import com.locationbase.Service.LandmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LandmarkController {

    private final LandmarkService landmarkService;

    @Autowired
    public LandmarkController(LandmarkService landmarkService) {
        this.landmarkService = landmarkService;
    }

    // /landmark 요청이 들어오면 SelectLandmark.html 반환
    @GetMapping("/landmark")
    public String showSelectLandmark(@RequestParam("plannerId") int plannerId) {
        // Service를 호출하여 비즈니스 로직 수행 (필요 시)
        String info = landmarkService.getLandmarkInfo();
        System.out.println("Planner ID: " + plannerId); // 디버깅용 로그
        System.out.println(info); // 디버깅용 로그
        return "SelectLandmark"; // templates/SelectLandmark.html 반환
    }
}
