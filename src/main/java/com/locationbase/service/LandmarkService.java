package com.locationbase.service;

import com.locationbase.Domain.repository.LandMarkRepository;
import com.locationbase.entity.LandmarkEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LandmarkService {

    private final LandMarkRepository landmarkRepository;

    @Autowired
    public LandmarkService(LandMarkRepository landmarkRepository) {
        this.landmarkRepository = landmarkRepository;
    }

    public Map<String, String> getLandmarkCoordinates(String landmarkName) {
        LandmarkEntity landmark = (LandmarkEntity) landmarkRepository.findByLandmarkName(landmarkName)
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
