package com.locationbase.Service;

import com.locationbase.Entity.PlannerEntity;
import com.locationbase.Entity.PlannerSpotEntity;
import com.locationbase.Domain.Repository.PlannerSpotRepository;
import com.locationbase.Domain.Repository.PlannerRepository;
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


    public List<PlannerSpotEntity> getSpotsByPlanner(int plannerId) {

        PlannerEntity planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner not found with id: " + plannerId));


        return plannerSpotRepository.findByPlanner(planner);
    }


    public PlannerSpotEntity getSpotById(int plannerSpotId) {
        return plannerSpotRepository.findById(plannerSpotId)
                .orElseThrow(() -> new RuntimeException("Spot not found with id: " + plannerSpotId));
    }


    public PlannerSpotEntity addSpot(int plannerId, String spotName, int visitOrder, int routeId) {
        // Find the planner by plannerId
        PlannerEntity planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner not found with id: " + plannerId));

        PlannerSpotEntity newSpot = new PlannerSpotEntity();
        newSpot.setPlanner(planner);
        newSpot.setSpotName(spotName);
        newSpot.setVisitOrder(visitOrder);
        newSpot.setRouteId(routeId);

        return plannerSpotRepository.save(newSpot);
    }


    public PlannerSpotEntity updateSpot(int plannerSpotId, String spotName, int visitOrder, int routeId) {
        PlannerSpotEntity existingSpot = getSpotById(plannerSpotId);
        existingSpot.setSpotName(spotName);
        existingSpot.setVisitOrder(visitOrder);
        existingSpot.setRouteId(routeId);
        return plannerSpotRepository.save(existingSpot);
    }


    public void deleteSpot(int plannerSpotId) {
        PlannerSpotEntity spot = getSpotById(plannerSpotId);
        plannerSpotRepository.delete(spot);
    }
}
