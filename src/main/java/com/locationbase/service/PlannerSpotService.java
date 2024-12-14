package com.locationbase.service;

import com.locationbase.Domain.repository.PlannerSpotRepository;
import com.locationbase.entity.PlannerSpotEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlannerSpotService {

    private static final Logger logger = LoggerFactory.getLogger(PlannerSpotService.class);

    private final PlannerSpotRepository plannerSpotRepository;

    @Autowired
    public PlannerSpotService(PlannerSpotRepository plannerSpotRepository) {
        this.plannerSpotRepository = plannerSpotRepository;
        logger.info("PlannerSpotService 생성 완료");
    }

    // PlannerSpot 저장
    public void savePlannerSpot(PlannerSpotEntity plannerSpot) {
        logger.info("PlannerSpot 저장 요청 시작: {}", plannerSpot);
        try {
            plannerSpotRepository.save(plannerSpot);
            logger.info("PlannerSpot 저장 성공: {}", plannerSpot);
        } catch (Exception e) {
            logger.error("PlannerSpot 저장 실패", e);
            throw e;
        }
    }

    // 플래너 ID에 따른 마지막 방문 순서 조회
    public Integer getLastVisitOrderByPlannerId(Integer plannerId) { // int -> Integer로 수정
        logger.info("플래너 ID로 마지막 방문 순서 조회 요청: plannerId={}", plannerId);
        try {
            Optional<Integer> maxVisitOrder = plannerSpotRepository.findMaxVisitOrderByPlannerId(plannerId);
            Integer result = maxVisitOrder.orElse(0);
            logger.info("마지막 방문 순서 조회 성공: plannerId={}, lastVisitOrder={}", plannerId, result);
            return result;
        } catch (Exception e) {
            logger.error("마지막 방문 순서 조회 실패: plannerId={}", plannerId, e);
            throw e;
        }
    }

    // planner_id로 PlannerSpot 데이터 조회 및 visit_order 기준 정렬
    public List<PlannerSpotEntity> getPlannerSpotsByPlannerId(Integer plannerId) {
        logger.info("PlannerSpot 조회 요청 시작: plannerId={}", plannerId);
        try {
            List<PlannerSpotEntity> plannerSpots = plannerSpotRepository.findByPlanner_PlannerIdOrderByVisitOrderAsc(plannerId);
            logger.info("PlannerSpot 조회 성공: 조회된 스팟 수={}, 데이터={}", plannerSpots.size(), plannerSpots);
            return plannerSpots;
        } catch (Exception e) {
            logger.error("PlannerSpot 조회 실패: plannerId={}", plannerId, e);
            throw e;
        }
    }
}