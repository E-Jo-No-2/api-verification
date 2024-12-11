package com.locationbase.service;

import com.locationbase.domain.repository.PlannerSpotRepository;
import com.locationbase.entity.PlannerSpotEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PlannerSpotService {

    private final PlannerSpotRepository plannerSpotRepository;

    public PlannerSpotService(PlannerSpotRepository plannerSpotRepository) {
        this.plannerSpotRepository = plannerSpotRepository;
    }

    @Transactional
    public void savePlannerSpot(PlannerSpotEntity plannerSpot) {
        // 현재 planner_id와 route_id에 해당하는 최대 visit_order 조회
        Integer maxVisitOrder = plannerSpotRepository.findMaxVisitOrderByPlannerAndRoute(
                plannerSpot.getPlanner().getPlannerId(), plannerSpot.getRoute().getRouteId());

        // 새로운 visit_order 설정
        int newVisitOrder = (maxVisitOrder != null ? maxVisitOrder : 0) + 1;
        plannerSpot.setVisitOrder(newVisitOrder);

        // PlannerSpotEntity 저장
        plannerSpotRepository.save(plannerSpot);
    }

    public Optional<PlannerSpotEntity> getSpotById(int id) {
        return plannerSpotRepository.findById(id);
    }

    public void deleteSpot(int id) {
        plannerSpotRepository.deleteById(id);
    }
}
