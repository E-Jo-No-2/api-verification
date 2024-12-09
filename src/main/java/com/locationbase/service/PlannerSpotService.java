package com.locationbase.service;

import com.locationbase.domain.repository.PlannerSpotRepository;
import com.locationbase.entity.PlannerSpotEntity;
import org.springframework.stereotype.Service;

@Service
public class PlannerSpotService {

    private final PlannerSpotRepository plannerSpotRepository;

    public PlannerSpotService(PlannerSpotRepository plannerSpotRepository) {
        this.plannerSpotRepository = plannerSpotRepository;
    }

    public void savePlannerSpot(String spotName) {
        PlannerSpotEntity spot = new PlannerSpotEntity();
        spot.setSpot_name(spotName);
        plannerSpotRepository.save(spot);
    }
}

