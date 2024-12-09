package com.locationbase.service;

import com.locationbase.domain.repository.PlacesRepository;
import com.locationbase.domain.repository.RouteRepository;
import com.locationbase.dto.RouteDTO;
import com.locationbase.entity.PlacesEntity;
import com.locationbase.entity.RouteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteService {
    private final RouteRepository routeRepository;
    private final PlacesRepository placesRepository;

    public RouteService(RouteRepository routeRepository, PlacesRepository placesRepository) {
        this.routeRepository = routeRepository;
        this.placesRepository = placesRepository;
    }

    @Transactional
    public void saveRoute(RouteDTO routeDTO) {
        System.out.println("[INPUT] Service received RouteDTO: " + routeDTO);

        PlacesEntity startPlace = placesRepository.findById(routeDTO.getStartPoint())
                .orElseThrow(() -> new IllegalArgumentException("Invalid start point ID: " + routeDTO.getStartPoint()));
        PlacesEntity endPlace = routeDTO.getEndPoint() != null ?
                placesRepository.findById(routeDTO.getEndPoint())
                        .orElse(null) : null;

        RouteEntity route = new RouteEntity();
        route.setStart_point(startPlace);
        route.setEnd_point(endPlace);
        route.setThemeName(routeDTO.getThemeName());
        route.setTaxi_fare(routeDTO.getTaxiFare());
        route.setEstimated_time(routeDTO.getEstimatedTime());
        route.setDistance(routeDTO.getDistance());

        System.out.println("[OUTPUT] Saving RouteEntity: " + route);

        routeRepository.save(route);
    }
}
