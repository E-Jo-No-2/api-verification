package com.locationbase.service;

import com.locationbase.entity.PlacesEntity;
import com.locationbase.domain.repository.PlacesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlacesService {

    @Autowired
    private PlacesRepository placesRepository;

    public String savePlace(PlacesEntity place) {
        // 중복 데이터 체크
        Optional<PlacesEntity> existingPlace = placesRepository.findByNameAndLatAndLng(
                place.getName(), place.getLat(), place.getLng());

        if (existingPlace.isPresent()) {
            return "Place already exists!"; // 중복 메시지 반환
        }

        // 중복이 없으면 저장
        placesRepository.save(place);
        return "Place saved successfully!";
    }
}
