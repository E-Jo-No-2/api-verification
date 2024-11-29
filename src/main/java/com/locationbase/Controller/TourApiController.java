package com.locationbase.Controller;

import com.locationbase.DTO.LandMarkDTO;
import com.locationbase.Service.TourApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/themaselect")
public class TourApiController {

    private final TourApiService tourApiService;

    public TourApiController(TourApiService tourApiService) {
        this.tourApiService = tourApiService;
    }

    @GetMapping("/nearby")
    public Map<String, List<LandMarkDTO>> getNearbyLandmarks(
            @RequestParam String latitude,
            @RequestParam String longitude,
            @RequestParam(defaultValue = "3500") double radius) {
        return tourApiService.getNearbySpotsByTheme(latitude, longitude, radius);
    }
}
