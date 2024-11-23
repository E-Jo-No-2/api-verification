package com.locationbase.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DirectionsController {
    private static final Logger logger = LoggerFactory.getLogger(DirectionsController.class);

    @GetMapping("/map") // /map 요청에 대해 map.html 반환
    public String showMapPage(Model model) {
        model.addAttribute("path", List.of());
        return "map"; // map.html 반환
    }
}
