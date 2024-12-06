package com.locationbase.controller;

import com.locationbase.entity.PlacesEntity;
import com.locationbase.service.PlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/places")
public class PlacesController {

    @Autowired
    private PlacesService placesService;

    @PostMapping("/save")
    public ResponseEntity<PlacesEntity> savePlace(@RequestBody PlacesEntity place) {
        PlacesEntity savedPlace = placesService.savePlace(place);
        return ResponseEntity.ok(savedPlace);
    }
}
