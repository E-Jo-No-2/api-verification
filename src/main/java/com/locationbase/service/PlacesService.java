package com.locationbase.service;

import com.locationbase.entity.PlacesEntity;
import com.locationbase.domain.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlacesService {

    @Autowired
    private PlaceRepository placeRepository;

    public PlacesEntity savePlace(PlacesEntity place) {
        return placeRepository.save(place);
    }
}
