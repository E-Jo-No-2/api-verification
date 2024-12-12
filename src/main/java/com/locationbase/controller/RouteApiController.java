package com.locationbase.controller;

import com.locationbase.dto.RouteDTO;
import com.locationbase.service.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/route")
public class RouteApiController {

    private static final Logger logger = LoggerFactory.getLogger(RouteApiController.class);

    private final RouteService routeService;

    public RouteApiController(RouteService routeService) {
        this.routeService = routeService;
    }

    /**
     * 새로운 Route를 저장합니다.
     * @param routeDTO 요청 데이터
     * @return 성공 또는 실패 메시지
     */
    @PostMapping("/save")
    public ResponseEntity<String> saveRoute(@RequestBody RouteDTO routeDTO) {
        try {
            logger.debug("[입력] RouteDTO 수신됨: {}", routeDTO);

            routeService.saveRoute(routeDTO);

            logger.debug("[출력] RouteDTO에 대한 경로가 성공적으로 저장됨: {}", routeDTO);
            return ResponseEntity.ok("경로가 성공적으로 저장되었습니다!");
        } catch (Exception e) {
            logger.error("경로 저장 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("경로 저장에 실패하였습니다.");
        }
    }
}
