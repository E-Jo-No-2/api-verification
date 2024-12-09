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

        PlacesEntity startPlace = placeRepository.findById(routeDTO.getStartPoint())
                .orElseThrow(() -> new IllegalArgumentException("Invalid start point ID: " + routeDTO.getStartPoint()));
        PlacesEntity endPlace = routeDTO.getEndPoint() != null ?
                placeRepository.findById(routeDTO.getEndPoint())
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
