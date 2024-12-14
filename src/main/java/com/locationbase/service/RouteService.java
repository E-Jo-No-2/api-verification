package com.locationbase.service;

import com.locationbase.Domain.repository.PlacesRepository;
import com.locationbase.Domain.repository.RouteRepository;
import com.locationbase.Domain.repository.PlannerRepository;
import com.locationbase.dto.RouteDTO;
import com.locationbase.entity.PlacesEntity;
import com.locationbase.entity.PlannerEntity;
import com.locationbase.entity.RouteEntity;
import com.locationbase.entity.PlannerSpotEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RouteService {

    private static final Logger logger = LoggerFactory.getLogger(RouteService.class);

    private final RouteRepository routeRepository;
    private final PlacesRepository placesRepository;
    private final PlannerRepository plannerRepository;
    private final PlannerSpotService plannerSpotService;

    @Autowired
    public RouteService(RouteRepository routeRepository, PlacesRepository placesRepository, PlannerRepository plannerRepository, PlannerSpotService plannerSpotService) {
        this.routeRepository = routeRepository;
        this.placesRepository = placesRepository;
        this.plannerRepository = plannerRepository;
        this.plannerSpotService = plannerSpotService;
    }

    @Transactional
    public void saveRoute(RouteDTO routeDTO) {
        logger.debug("[입력] RouteDTO로 경로 생성 중: {}", routeDTO);

        // startPoint와 endPoint를 PlacesEntity로 변환
        PlacesEntity startPlace = placesRepository.findById(routeDTO.getStartPoint())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 출발 지점 ID: " + routeDTO.getStartPoint()));
        PlacesEntity endPlace = placesRepository.findById(routeDTO.getEndPoint())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 도착 지점 ID: " + routeDTO.getEndPoint()));
        logger.debug("출발 지점: {}, 도착 지점: {}", startPlace, endPlace);

        // PlannerEntity 설정
        PlannerEntity planner = null;
        if (routeDTO.getPlannerId() != null) {
            planner = plannerRepository.findById(routeDTO.getPlannerId())
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 플래너 ID: " + routeDTO.getPlannerId()));
            logger.debug("플래너: {}", planner);
        }

        // 중복된 경로 확인
        boolean exists = routeRepository.existsByStartPointAndEndPoint(startPlace, endPlace);
        if (exists) {
            logger.warn("중복된 경로가 존재합니다: 출발 지점={}, 도착 지점={}", startPlace.getPlaceId(), endPlace.getPlaceId());
            throw new IllegalArgumentException("중복된 경로가 존재합니다: 출발 지점=" + startPlace.getPlaceId() + ", 도착 지점=" + endPlace.getPlaceId());
        }

        // RouteEntity 생성 및 저장
        RouteEntity route = new RouteEntity();
        route.setStartPoint(startPlace);
        route.setEndPoint(endPlace);
        route.setThemeName(routeDTO.getThemeName());
        route.setPlanner(planner);

        logger.debug("[디버그] RouteEntity 저장 중: 출발 지점={}, 도착 지점={}", startPlace.getPlaceId(), endPlace.getPlaceId());
        routeRepository.save(route);
        routeRepository.flush();  // 명시적으로 커밋하여 즉시 저장

        // PlannerSpotEntity 생성 및 저장
        PlannerSpotEntity plannerSpot = new PlannerSpotEntity();
        plannerSpot.setSpotName("New Spot");  // Spot 이름 설정
        plannerSpot.setLatitude(endPlace.getLat());
        plannerSpot.setLongitude(endPlace.getLng());
        plannerSpot.setPlanner(planner);
        plannerSpot.setPlace(endPlace);
        plannerSpot.setRoute(route);

        // 방문 순서 설정
        int lastVisitOrder = plannerSpotService.getLastVisitOrderByPlannerId(routeDTO.getPlannerId());
        plannerSpot.setVisitOrder(lastVisitOrder + 1);

        logger.debug("PlannerSpotEntity 저장 시도: {}", plannerSpot);
        plannerSpotService.savePlannerSpot(plannerSpot);

        // 로그 추가: 경로 저장 후의 상태 확인
        logger.debug("[확인] 경로 저장 완료: {}", route);
    }

    @Transactional(readOnly = true)
    public Optional<RouteEntity> findByStartPointAndEndPoint(Integer startPoint, Integer endPoint) {
        PlacesEntity startPlace = placesRepository.findById(startPoint)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 출발 지점 ID: " + startPoint));
        PlacesEntity endPlace = placesRepository.findById(endPoint)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 도착 지점 ID: " + endPoint));
        logger.debug("조회 시 출발 지점: {}, 도착 지점: {}", startPlace, endPlace);

        // 로그 추가: 경로 조회 시도
        logger.debug("[디버그] 경로 조회 시도: 출발 지점={}, 도착 지점={}", startPlace.getPlaceId(), endPlace.getPlaceId());
        return routeRepository.findByStartPointAndEndPoint(startPlace, endPlace);
    }
}
