package com.locationbase.Controller;

import com.locationbase.Service.ThemaSelectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThemaSelectController {

    private final ThemaSelectService themaSelectService;

    public ThemaSelectController(ThemaSelectService themaSelectService) {
        this.themaSelectService = themaSelectService;
    }

    @GetMapping("/themaselect") // 요청 URL: http://localhost:8091/themaselect
    public String themaselect(Model model) {
        String themeData = themaSelectService.getThemeData(); // Service에서 데이터 가져오기
        model.addAttribute("themeData", themeData); // 데이터를 Model에 추가
        return "ThemaSelects"; // templates/ThemaSelects.html 파일 렌더링
    }
}
