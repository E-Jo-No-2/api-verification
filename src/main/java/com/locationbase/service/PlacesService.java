package com.locationbase.service;

import com.locationbase.entity.PlacesEntity;
import com.locationbase.domain.repository.PlacesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlacesService {

    @Autowired
    private PlacesRepository placesRepository;

    public PlacesEntity savePlace(PlacesEntity place) {
        return placesRepository.save(place);
    }
}
