package com.locationbase.controller;

import com.locationbase.entity.PlacesEntity;
import com.locationbase.entity.PlannerEntity;
import com.locationbase.entity.PlannerSpotEntity;
import com.locationbase.entity.RouteEntity;
import com.locationbase.service.PlannerSpotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/planner-spot")
public class PlannerSpotController {

    private static final Logger logger = LoggerFactory.getLogger(PlannerSpotController.class);

    private final PlannerSpotService plannerSpotService;

    public PlannerSpotController(PlannerSpotService plannerSpotService) {
        this.plannerSpotService = plannerSpotService;
    }

    /**
     * 새로운 PlannerSpot을 저장합니다.
     * @param requestData 요청 데이터 (spot_name, planner_id, route_id, place_id, latitude, longitude 포함)
     * @return 성공 또는 실패 메시지
     */
    @PostMapping("/save")
    public ResponseEntity<String> savePlannerSpot(@RequestBody Map<String, Object> requestData) {
        try {
            // 요청 데이터에서 필드 추출
            String spotName = (String) requestData.get("spot_name");
            Integer plannerId = (Integer) requestData.get("planner_id");
            Integer routeId = (Integer) requestData.get("route_id");
            Integer placeId = (Integer) requestData.get("place_id");
            String latitude = (String) requestData.get("latitude");
            String longitude = (String) requestData.get("longitude");

            // PlannerSpotEntity 생성 및 필드 설정
            PlannerSpotEntity plannerSpot = new PlannerSpotEntity();
            plannerSpot.setSpotName(spotName);
            plannerSpot.setLatitude(latitude);
            plannerSpot.setLongitude(longitude);

            // PlannerEntity 설정
            PlannerEntity planner = new PlannerEntity();
            planner.setPlannerId(plannerId);
            plannerSpot.setPlanner(planner);

            // RouteEntity 설정
            RouteEntity route = new RouteEntity();
            route.setRouteId(routeId);
            plannerSpot.setRoute(route);

            // PlacesEntity 설정
            PlacesEntity place = new PlacesEntity();
            place.setPlaceId(placeId);
            plannerSpot.setPlace(place);

            // PlannerSpot 테이블 저장
            plannerSpotService.savePlannerSpot(plannerSpot);

            return ResponseEntity.ok("플래너 스팟이 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            logger.error("플래너 스팟 저장 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("플래너 스팟 저장에 실패하였습니다.");
        }
    }

    /**
     * 특정 플래너 ID에 해당하는 모든 PlannerSpot 데이터를 반환합니다.
     * @param plannerId 플래너 ID
     * @return PlannerSpot 데이터 리스트
     */
    @GetMapping("/list")
    public ResponseEntity<?> getPlannerSpots(@RequestParam("planner_id") Integer plannerId) {
        try {
            // 플래너 ID에 해당하는 데이터 가져오기
            List<PlannerSpotEntity> spots = plannerSpotService.findPlannerSpotsByPlannerId(plannerId);
            return ResponseEntity.ok(spots);
        } catch (Exception e) {
            logger.error("플래너 스팟 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터를 가져오는 중 오류가 발생했습니다.");
        }
    }

}
