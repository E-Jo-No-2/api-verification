
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
        logger.info("PlannerSpot 저장 요청 수신: 요청 데이터={}", requestData);
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

            logger.info("PlannerSpot 저장 성공: {}");
            return ResponseEntity.ok("플래너 스팟이 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            logger.error("플래너 스팟 저장 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("플래너 스팟 저장에 실패하였습니다.");
        }

    }
    @GetMapping("/list/{plannerId}")
    public ResponseEntity<?> getPlannerSpotsByPlannerId(@PathVariable("plannerId") Integer plannerId) {
        logger.info("PlannerSpot 조회 요청 수신: plannerId={}", plannerId);
        try {
            List<PlannerSpotEntity> plannerSpots = plannerSpotService.getPlannerSpotsByPlannerId(plannerId);
            logger.info("PlannerSpot 조회 성공: plannerId={}, 조회된 스팟 수={}", plannerId, plannerSpots.size());
            return ResponseEntity.ok(plannerSpots);
        } catch (Exception e) {
            logger.error("PlannerSpot 조회 실패: plannerId={}", plannerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("플래너 스팟 데이터를 로드하지 못했습니다.");
        }
    }
}
