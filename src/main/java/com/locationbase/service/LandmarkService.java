package com.locationbase.service;

import com.locationbase.domain.repository.LandmarkRepository;
import com.locationbase.entity.LandmarkEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LandmarkService {

    private final LandmarkRepository landmarkRepository;

    @Autowired
    public LandmarkService(LandmarkRepository landmarkRepository) {
        this.landmarkRepository = landmarkRepository;
    }

    public Map<String, String> getLandmarkCoordinates(String landmarkName) {
        LandmarkEntity landmark = landmarkRepository.findByLandmarkName(landmarkName)
                .orElse(null);

        if (landmark == null) {
            return null;
        }

        Map<String, String> coordinates = new HashMap<>();
        coordinates.put("latitude", landmark.getLatitude());
        coordinates.put("longitude", landmark.getLongitude());

        return coordinates;
    }
}
