package com.locationbase.service;

import com.locationbase.Domain.repository.RouteRepository;
import com.locationbase.dto.RouteDTO;
import com.locationbase.entity.RouteEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteService {
    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Transactional
    public void saveRoute(RouteDTO routeDTO) {
        System.out.println("[INPUT] Service received RouteDTO: " + routeDTO);

        RouteEntity route = new RouteEntity();
        route.setStart_point(routeDTO.getStart_point());
        route.setEnd_point(routeDTO.getEnd_point());
        route.setThemeName(routeDTO.getTheme_name());

        System.out.println("[OUTPUT] Saving RouteEntity: " + route);

        routeRepository.save(route);
    }

}


