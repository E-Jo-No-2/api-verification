package com.locationbase.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DirectionsController {

    @GetMapping("/map") // /map 요청에 대해 map.html 반환
    public String showMapPage(Model model) {
        // 초기 빈 데이터를 전달 (경로 데이터가 없더라도 템플릿에서 처리 가능)
        model.addAttribute("path", List.of());
        return "map"; // map.html 반환
    }
}
