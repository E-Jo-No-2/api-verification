package com.locationbase.controller;

import com.locationbase.dto.RouteDTO;
import com.locationbase.entity.PlacesEntity;
import com.locationbase.entity.PlannerEntity;
import com.locationbase.entity.PlannerSpotEntity;
import com.locationbase.entity.RouteEntity;
import com.locationbase.service.PlacesService;
import com.locationbase.service.PlannerService;
import com.locationbase.service.RouteService;
import com.locationbase.service.PlannerSpotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/places")
public class PlacesController {

    private static final Logger logger = LoggerFactory.getLogger(PlacesController.class);

    private final PlacesService placesService;
    private final RouteService routeService;
    private final PlannerService plannerService;
    private final PlannerSpotService plannerSpotService;
    private Integer lastSelectedPlaceId = null;
    //private Integer plannerId = 3; // 플래너 ID를 1로 설정

    @Autowired
    public PlacesController(PlacesService placesService, RouteService routeService, PlannerService plannerService, PlannerSpotService plannerSpotService) {
        this.placesService = placesService;
        this.routeService = routeService;
        this.plannerService = plannerService;
        this.plannerSpotService = plannerSpotService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> savePlace(@RequestBody PlacesEntity place) {
        try {
            // 장소 저장
            String result = placesService.savePlace(place);

            // 중복 데이터일 경우
            if (result.equals("Place already exists!")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"장소가 이미 존재합니다!\"}");
            }

            // 저장된 Place의 ID 가져오기
            Optional<PlacesEntity> savedPlace = placesService.getPlaceByNameAndCoordinates(
                    place.getName(), place.getLat(), place.getLng());
            if (savedPlace.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"message\":\"장소 저장 후 ID를 찾을 수 없습니다.\"}");
            }

            Integer currentPlaceId = savedPlace.get().getPlaceId();

            // 플래너 존재 여부 확인
            Optional<PlannerEntity> plannerOptional = plannerService.findById(plannerId);
            if (plannerOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\":\"유효하지 않은 플래너 ID: " + plannerId + "\"}");
            }

            RouteDTO routeDTO = null;
            if (lastSelectedPlaceId != null && !lastSelectedPlaceId.equals(currentPlaceId)) {
                logger.debug("경로 생성 중: 출발 지점={}, 도착 지점={}, 플래너 ID={}", lastSelectedPlaceId, currentPlaceId, plannerId);
                routeDTO = new RouteDTO();
                routeDTO.setStartPoint(lastSelectedPlaceId);
                routeDTO.setEndPoint(currentPlaceId);
                routeDTO.setThemeName("defaultTheme");
                routeDTO.setPlannerId(plannerId);
                routeService.saveRoute(routeDTO);

                // 로그 추가: 경로 조회 시도
                logger.debug("경로 조회 시도: start_point={}, end_point={}", lastSelectedPlaceId, currentPlaceId);
            } else {
                logger.debug("첫 번째 장소가 선택되었거나 출발 지점과 도착 지점이 동일하여 경로가 생성되지 않았습니다.");
            }

            // 마지막 선택된 place_id 업데이트
            lastSelectedPlaceId = currentPlaceId;

            // PlannerSpot 저장
            if (routeDTO != null) {
                PlannerSpotEntity plannerSpot = new PlannerSpotEntity();
                plannerSpot.setSpotName(place.getName());
                plannerSpot.setLatitude(place.getLat());
                plannerSpot.setLongitude(place.getLng());
                plannerSpot.setPlanner(plannerOptional.get());
                plannerSpot.setPlace(savedPlace.get());
                // RouteEntity 설정
                logger.debug("PlannerSpot 저장 시도: lastSelectedPlaceId={}, currentPlaceId={}", lastSelectedPlaceId, currentPlaceId);
                RouteEntity routeEntity = routeService.findByStartPointAndEndPoint(lastSelectedPlaceId, currentPlaceId).orElseThrow(
                        () -> new RuntimeException("경로 정보를 찾을 수 없습니다.")
                );
                plannerSpot.setRoute(routeEntity);

                plannerSpotService.savePlannerSpot(plannerSpot);
            }

            return ResponseEntity.ok("{\"message\":\"장소가 성공적으로 저장되었습니다!\"}");
        } catch (Exception e) {
            logger.error("장소 저장 중 오류 발생: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"알 수 없는 오류가 발생했습니다.\"}");
        }
    }
}
