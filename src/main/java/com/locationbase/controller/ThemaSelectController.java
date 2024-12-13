package com.locationbase.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
public class ThemaSelectController {

    @GetMapping("/themaselect") // 요청 URL: http://localhost:8091/themaselect
    public String themaselect() {
        return "ThemaSelects"; // templates/ThemaSelects.html 파일 렌더링
    }

    @PostMapping("/save-theme")
    @ResponseBody
    public ResponseEntity<String> saveTheme(@RequestBody Map<String, String> request) {
        String theme = request.get("theme");

        if (theme == null || theme.isBlank()) {
            return ResponseEntity.badRequest().body("테마 정보가 없습니다.");
        }


        try {
            // 테마를 저장하거나 처리하는 로직 추가 (예: DB에 저장)
            System.out.println("Selected Theme: " + theme); // 로그 출력

            // 성공 응답
            return ResponseEntity.ok("Theme saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("테마 저장 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/thema-map")
    public String showMapPage() {
        return "map"; // map.html
    }
}


