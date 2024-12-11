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
    public void savePlannerSpot(String spotName) {
        PlannerSpotEntity spot = new PlannerSpotEntity();
        spot.setSpotName(spotName);
        plannerSpotRepository.save(spot);
    }

    public Optional<PlannerSpotEntity> getSpotById(int id) {
        return plannerSpotRepository.findById(id);
    }

    public void deleteSpot(int id) {
        plannerSpotRepository.deleteById(id);
    }
}
