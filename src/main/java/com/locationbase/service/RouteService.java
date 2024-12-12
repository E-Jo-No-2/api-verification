package com.locationbase.service;

import com.locationbase.domain.repository.PlacesRepository;
import com.locationbase.domain.repository.RouteRepository;
import com.locationbase.dto.RouteDTO;
import com.locationbase.entity.PlacesEntity;
import com.locationbase.entity.RouteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private PlacesRepository placesRepository;

    @Transactional
    public void saveRoute(RouteDTO routeDTO) {
        System.out.println("[INPUT] Creating Route with DTO: " + routeDTO);

        // startPoint와 endPoint를 PlacesEntity로 변환
        PlacesEntity startPlace = placesRepository.findById(routeDTO.getStartPoint())
                .orElseThrow(() -> new IllegalArgumentException("Invalid start point ID: " + routeDTO.getStartPoint()));
        PlacesEntity endPlace = placesRepository.findById(routeDTO.getEndPoint())
                .orElseThrow(() -> new IllegalArgumentException("Invalid end point ID: " + routeDTO.getEndPoint()));

        // RouteEntity 생성 및 저장
        RouteEntity route = new RouteEntity();
        route.setStart_point(startPlace);
        route.setEnd_point(endPlace);
        route.setThemeName(routeDTO.getThemeName());
        routeRepository.save(route);

        System.out.println("[DEBUG] RouteEntity being saved: start_point="
                + startPlace.getPlaceId() + ", end_point=" + endPlace.getPlaceId());
        routeRepository.save(route);

    }
}
