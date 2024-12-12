package com.locationbase.service;

import com.locationbase.domain.repository.PlannerRepository;
import com.locationbase.domain.repository.PlannerSpotRepository;
import com.locationbase.entity.PlannerEntity;
import com.locationbase.entity.PlannerSpotEntity;
import org.springframework.stereotype.Service;

@Service
public class PlannerSpotService {

    private final PlannerSpotRepository plannerSpotRepository;
    private final PlannerRepository plannerRepository; //plannerId 저장

    public PlannerSpotService(PlannerSpotRepository plannerSpotRepository, PlannerRepository plannerRepository) {
        this.plannerSpotRepository = plannerSpotRepository;
        this.plannerRepository = plannerRepository;
    }

    public void savePlannerSpot(PlannerSpotEntity plannerSpot) { // 수정된 메서드 서명

        // PlannerSpot 저장
        plannerSpotRepository.save(plannerSpot);
    }
}
