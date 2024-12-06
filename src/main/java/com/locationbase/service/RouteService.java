package com.locationbase.service;

import com.locationbase.domain.repository.PlaceRepository;
import com.locationbase.domain.repository.RouteRepository;
import com.locationbase.dto.RouteDTO;
import com.locationbase.entity.PlacesEntity;
import com.locationbase.entity.RouteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteService {
    private final RouteRepository routeRepository;
    private final PlaceRepository placeRepository;

    public RouteService(RouteRepository routeRepository, PlaceRepository placeRepository) {
        this.routeRepository = routeRepository;
        this.placeRepository = placeRepository;
    }

    @Transactional
    public void saveRoute(RouteDTO routeDTO) {
        System.out.println("[INPUT] Service received RouteDTO: " + routeDTO);

        PlacesEntity startPlace = placeRepository.findById(routeDTO.getStart_point())
                .orElseThrow(() -> new IllegalArgumentException("Invalid start point ID: " + routeDTO.getStart_point()));
        PlacesEntity endPlace = routeDTO.getEnd_point() != null ?
                placeRepository.findById(routeDTO.getEnd_point())
                        .orElse(null) : null;

        RouteEntity route = new RouteEntity();
        route.setStart_point(startPlace);
        route.setEnd_point(endPlace);
        route.setThemeName(routeDTO.getTheme_name());
        route.setTaxi_fare(routeDTO.getTaxi_fare());
        route.setEstimated_time(routeDTO.getEstimated_time());
        route.setDistance(routeDTO.getDistance());

        System.out.println("[OUTPUT] Saving RouteEntity: " + route);

        routeRepository.save(route);
    }
}
