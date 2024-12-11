package com.locationbase.service;

import com.locationbase.domain.repository.PlacesRepository;
import com.locationbase.domain.repository.RouteRepository;
import com.locationbase.dto.RouteDTO;
import com.locationbase.entity.PlacesEntity;
import com.locationbase.entity.RouteEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RouteService {

    private static final Logger logger = LoggerFactory.getLogger(RouteService.class);

    private final RouteRepository routeRepository;
    private final PlacesRepository placesRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository, PlacesRepository placesRepository) {
        this.routeRepository = routeRepository;
        this.placesRepository = placesRepository;
    }

    @Transactional
    public void saveRoute(RouteDTO routeDTO) {
        logger.debug("[입력] RouteDTO로 경로 생성 중: {}", routeDTO);

        // startPoint와 endPoint를 PlacesEntity로 변환
        PlacesEntity startPlace = placesRepository.findById(routeDTO.getStartPoint())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 출발 지점 ID: " + routeDTO.getStartPoint()));
        PlacesEntity endPlace = placesRepository.findById(routeDTO.getEndPoint())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 도착 지점 ID: " + routeDTO.getEndPoint()));

        // RouteEntity 생성 및 저장
        RouteEntity route = new RouteEntity();
        route.setStartPoint(startPlace);
        route.setEndPoint(endPlace);
        route.setThemeName(routeDTO.getThemeName());

        logger.debug("[디버그] RouteEntity 저장 중: start_point={}, end_point={}", startPlace.getPlaceId(), endPlace.getPlaceId());
        routeRepository.save(route);
    }
}
