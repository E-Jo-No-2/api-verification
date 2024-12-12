package com.locationbase.service;

import com.locationbase.domain.repository.PlannerSpotRepository;
import com.locationbase.entity.PlannerSpotEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
