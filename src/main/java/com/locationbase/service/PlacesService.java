package com.locationbase.service;

import com.locationbase.domain.repository.PlacesRepository;
import com.locationbase.entity.PlacesEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlacesService {

    private static final String PLACE_EXISTS_MESSAGE = "장소가 이미 존재합니다!";
    private static final String PLACE_SAVED_SUCCESSFULLY_MESSAGE = "장소가 성공적으로 저장되었습니다!";

    private final PlacesRepository placesRepository;

    @Autowired
    public PlacesService(PlacesRepository placesRepository) {
        this.placesRepository = placesRepository;
    }

    // 장소 저장
    public String savePlace(PlacesEntity place) {
        // 중복 확인
        Optional<PlacesEntity> existingPlace = placesRepository.findByNameAndLatAndLng(place.getName(), place.getLat(), place.getLng());
        if (existingPlace.isPresent()) {
            return PLACE_EXISTS_MESSAGE;
        }
        placesRepository.save(place);
        return PLACE_SAVED_SUCCESSFULLY_MESSAGE;
    }

    // 장소 조회
    public Optional<PlacesEntity> getPlaceByNameAndCoordinates(String name, String lat, String lng) {
        return placesRepository.findByNameAndLatAndLng(name, lat, lng);
    }
}
