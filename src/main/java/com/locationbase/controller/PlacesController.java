package com.locationbase.controller;

import com.locationbase.entity.PlacesEntity;
import com.locationbase.service.PlacesService;
import com.locationbase.service.RouteService;
import com.locationbase.dto.RouteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@RestController
@RequestMapping("/api/places")
public class PlacesController {

    private static final Logger logger = LoggerFactory.getLogger(PlacesController.class);

    private final PlacesService placesService;
    private final RouteService routeService;
    private Integer lastSelectedPlaceId = null; // 마지막 선택된 place_id를 저장

    @Autowired
    public PlacesController(PlacesService placesService, RouteService routeService) {
        this.placesService = placesService;
        this.routeService = routeService;
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

            if (lastSelectedPlaceId != null) {
                logger.debug("경로 생성 중: 출발 지점={}, 도착 지점={}", lastSelectedPlaceId, currentPlaceId);
                RouteDTO routeDTO = new RouteDTO();
                routeDTO.setStartPoint(lastSelectedPlaceId);
                routeDTO.setEndPoint(currentPlaceId);
                routeDTO.setThemeName("defaultTheme");
                routeService.saveRoute(routeDTO);
            } else {
                logger.debug("첫 번째 장소가 선택되었으며, 경로가 생성되지 않았습니다.");
            }

            // 마지막 선택된 place_id 업데이트
            lastSelectedPlaceId = currentPlaceId;

            return ResponseEntity.ok("{\"message\":\"장소가 성공적으로 저장되었습니다!\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\":\"알 수 없는 오류가 발생했습니다.\"}");
        }
    }
}
