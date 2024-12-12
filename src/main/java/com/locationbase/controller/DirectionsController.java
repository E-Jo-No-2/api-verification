package com.locationbase.controller;

import com.locationbase.service.DirectionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class DirectionsController {

    private final DirectionsService directionsService;

    public DirectionsController(DirectionsService directionsService) {
        this.directionsService = directionsService;
    }

    @GetMapping("/map")
    public String showMapPage() {
        return "map"; // map.html
    }

    @GetMapping("/get-routes")
    @ResponseBody
    public ResponseEntity<?> getRoutes(@RequestParam("plannerId") int plannerId) {
        try {
            // RouteService에서 plannerId에 해당하는 경로 데이터를 가져오기
            List<Map<String, Object>> routes = directionsService.getRoutesByPlannerId(plannerId);
            return ResponseEntity.ok(routes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching routes: " + e.getMessage());
        }
    }

    @PostMapping("/calculate-route")
    @ResponseBody
    public ResponseEntity<?> calculateRoute(
            @RequestParam("start") String start,
            @RequestParam("goal") String goal,
            @RequestParam(value = "waypoints", required = false, defaultValue = "") String waypoints) {

        // 입력 값 검증
        if (start.isBlank() || goal.isBlank()) {
            return ResponseEntity.badRequest().body("Start and goal locations must be provided.");
        }

        // Waypoints를 리스트로 변환
        List<String> waypointsList = waypoints.isBlank() ? List.of() : List.of(waypoints.split("\\|"));

        try {
            // DirectionsService 호출
            Map<String, Object> routeOptions = directionsService.getRouteWithAllOptions(start, goal, waypointsList);

            // 경로 데이터를 로깅
            System.out.println("Route Data: " + routeOptions); // 로그 추가

            // 혼잡도 정보 추가
            Map<String, Object> trafficCongestionData = directionsService.getTrafficCongestionData(routeOptions);
            routeOptions.put("trafficCongestion", trafficCongestionData);

            // 성공적인 응답 반환
            return ResponseEntity.ok(routeOptions);

        } catch (Exception e) {
            // 오류 발생 시 클라이언트에 에러 메시지 반환
            return handleException("An error occurred while calculating the route.", e);
        }
    }

    @PostMapping("/traffic-info")
    @ResponseBody
    public ResponseEntity<?> getTrafficInfo(
            @RequestParam("route") List<String> routePoints) {

        // 입력 값 검증
        if (routePoints == null || routePoints.isEmpty()) {
            return ResponseEntity.badRequest().body("Route points must be provided.");
        }

        try {
            // DirectionsService에서 혼잡도 데이터 가져오기
            Map<String, Object> trafficInfo = directionsService.getTrafficInfoWithRoadNameAndCongestion(routePoints);

            // 성공적인 응답 반환
            return ResponseEntity.ok(trafficInfo);

        } catch (Exception e) {
            // 오류 발생 시 클라이언트에 에러 메시지 반환
            return handleException("An error occurred while fetching traffic information.", e);
        }
    }

    private ResponseEntity<?> handleException(String message, Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(message + " Error details: " + e.getMessage());
    }
}
