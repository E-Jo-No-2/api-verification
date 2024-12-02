package com.locationbase.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThemaSelectController {

    @GetMapping("/themaselect") // 요청 URL: http://localhost:8091/themaselect
    public String themaselect() {
        return "ThemaSelects"; // templates/ThemaSelects.html 파일 렌더링
    }
}
