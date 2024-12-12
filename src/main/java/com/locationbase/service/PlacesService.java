package com.locationbase.service;

import com.locationbase.domain.repository.PlacesRepository;
import com.locationbase.entity.PlacesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlacesService {

    @Autowired
    private PlacesRepository placesRepository;

    // 장소 저장
    public String savePlace(PlacesEntity place) {
        // 중복 확인
        Optional<PlacesEntity> existingPlace = placesRepository.findByNameAndLatAndLng(place.getName(), place.getLat(), place.getLng());
        if (existingPlace.isPresent()) {
            return "Place already exists!";
        }
        placesRepository.save(place);
        return "Place saved successfully!";
    }

    // 장소 조회
    public Optional<PlacesEntity> getPlaceByNameAndCoordinates(String name, String lat, String lng) {
        return placesRepository.findByNameAndLatAndLng(name, lat, lng);
    }


}
