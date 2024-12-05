package com.locationbase.controller;

import com.locationbase.dto.RouteDTO;
import com.locationbase.service.RouteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/route")
public class RouteApiController {
    private final RouteService routeService;

    public RouteApiController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveRoute(@RequestBody RouteDTO routeDTO) {
        System.out.println("[INPUT] RouteDTO received: " + routeDTO);

        routeService.saveRoute(routeDTO);

        System.out.println("[OUTPUT] Route saved successfully for RouteDTO: " + routeDTO);
        return ResponseEntity.ok("Route saved successfully!");
    }
}

