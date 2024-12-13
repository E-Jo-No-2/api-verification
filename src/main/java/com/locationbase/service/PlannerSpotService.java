package com.locationbase.service;

import com.locationbase.Domain.repository.PlannerSpotRepository;
import com.locationbase.entity.PlannerSpotEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PlannerSpotService {

    private final PlannerSpotRepository plannerSpotRepository;

    @Autowired
    public PlannerSpotService(PlannerSpotRepository plannerSpotRepository) {
        this.plannerSpotRepository = plannerSpotRepository;
    }

    public void savePlannerSpot(PlannerSpotEntity plannerSpot) {
        plannerSpotRepository.save(plannerSpot);
    }

    // 플래너 ID에 따른 마지막 방문 순서 조회
    public Integer getLastVisitOrderByPlannerId(int plannerId) {
        Optional<Integer> maxVisitOrder = plannerSpotRepository.findMaxVisitOrderByPlannerId(plannerId);
        return maxVisitOrder.orElse(0); // 값이 없을 경우 0을 반환
    }
}
