package com.locationbase.Controller;

import com.locationbase.Service.ThemaSelectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/theme")
public class ThemaSelectController {

    private final ThemaSelectService themaSelectService;

    @Autowired
    public ThemaSelectController(ThemaSelectService themaSelectService) {
        this.themaSelectService = themaSelectService;
    }

    // Endpoint to fetch theme data
    @GetMapping("/data")
    public ResponseEntity<String> getThemeData() {
        try {
            String themeData = themaSelectService.getThemeData();
            return ResponseEntity.ok(themeData);
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Error fetching theme data: " + e.getMessage());
        }
    }
}
