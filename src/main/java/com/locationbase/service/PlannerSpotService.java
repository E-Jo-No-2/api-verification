package com.locationbase.service;
/*
import com.locationbase.entity.PlannerEntity;
import com.locationbase.entity.PlannerSpotEntity;
import com.locationbase.Domain.repository.PlannerSpotRepository;
import com.locationbase.Domain.repository.PlannerRepository;  // Import PlannerRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlannerSpotService {

    private final PlannerSpotRepository plannerSpotRepository;
    private final PlannerRepository plannerRepository;

    @Autowired
    public PlannerSpotService(PlannerSpotRepository plannerSpotRepository, PlannerRepository plannerRepository) {
        this.plannerSpotRepository = plannerSpotRepository;
        this.plannerRepository = plannerRepository;
    }

    // Get all spots for a planner
    public List<PlannerSpotEntity> getSpotsByPlanner(int plannerId) {
        return plannerSpotRepository.findByPlanner_PlannerId(plannerId);
    }

    // Get a specific spot by its ID
    public PlannerSpotEntity getSpotById(int plannerSpotId) {
        return plannerSpotRepository.findById(plannerSpotId)
                .orElseThrow(() -> new RuntimeException("Spot not found with id: " + plannerSpotId));
    }

    // Add a new spot to the planner
    public PlannerSpotEntity addSpot(int plannerId, String spotName, int visitOrder, int routeId) {

        PlannerEntity planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner not found with id: " + plannerId));

        PlannerSpotEntity newSpot = new PlannerSpotEntity();
        newSpot.setPlanner(planner);  // Set the Planner entity
        newSpot.setSpotName(spotName);
        newSpot.setVisitOrder(visitOrder);
        newSpot.setRouteId(routeId);

        return plannerSpotRepository.save(newSpot);
    }

    // Update an existing spot
    public PlannerSpotEntity updateSpot(int plannerSpotId, String spotName, int visitOrder, int routeId) {
        PlannerSpotEntity existingSpot = getSpotById(plannerSpotId);
        existingSpot.setSpotName(spotName);
        existingSpot.setVisitOrder(visitOrder);
        existingSpot.setRouteId(routeId);
        return plannerSpotRepository.save(existingSpot);
    }

    // Delete a spot
    public void deleteSpot(int plannerSpotId) {
        PlannerSpotEntity spot = getSpotById(plannerSpotId);
        plannerSpotRepository.delete(spot);
    }
}*/

import com.locationbase.Domain.repository.PlannerSpotRepository;
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

