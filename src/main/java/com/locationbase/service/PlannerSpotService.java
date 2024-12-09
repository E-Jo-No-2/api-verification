package com.locationbase.service;

import com.locationbase.Domain.repository.PlannerRepository;
import com.locationbase.Domain.repository.PlannerSpotRepository;
import com.locationbase.entity.PlannerEntity;
import com.locationbase.entity.PlannerSpotEntity;
import org.springframework.stereotype.Service;

@Service
public class PlannerSpotService {

    private final PlannerSpotRepository plannerSpotRepository;
    private final PlannerRepository plannerRepository; //plannerId저장

    public PlannerSpotService(PlannerSpotRepository plannerSpotRepository, PlannerRepository plannerRepository) {
        this.plannerSpotRepository = plannerSpotRepository;
        this.plannerRepository = plannerRepository;
    }

    public void savePlannerSpot(String spotName,int plannerId) {//plannerId 저장

        // Planner ID로 PlannerEntity 조회
        PlannerEntity planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner를 찾을 수 없습니다: " + plannerId));

        PlannerSpotEntity spot = new PlannerSpotEntity();
        spot.setPlanner(planner);
        spot.setSpot_name(spotName);
        plannerSpotRepository.save(spot);
    }
}

