/*package com.locationbase.Service;

import com.locationbase.Domain.Repository.PlannerSpotRepository;
import com.locationbase.Entity.PlannerSpotEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PlannerSpotService {

    private final PlannerSpotRepository plannerSpotRepository;

    public PlannerSpotService(PlannerSpotRepository plannerSpotRepository) {
        this.plannerSpotRepository = plannerSpotRepository;
    }

    @Transactional(readOnly = false)
    public List<PlannerSpotEntity> getSpotsForPlanner(Integer plannerId) {
        return plannerSpotRepository.findByPlanner_PlannerIdOrderByVisitOrder(plannerId);
    }


    @Transactional
    public PlannerSpotEntity addSpot(PlannerSpotEntity plannerSpotEntity) {
        return plannerSpotRepository.save(plannerSpotEntity);
    }


    @Transactional
    public void removeSpot(Integer plannerSpotId) {
        plannerSpotRepository.deleteById(plannerSpotId);
    }
}*/

package com.locationbase.Service;

import com.locationbase.Entity.PlannerEntity;
import com.locationbase.Entity.PlannerSpotEntity;
import com.locationbase.Domain.Repository.PlannerSpotRepository;
import com.locationbase.Domain.Repository.PlannerRepository;  // Ensure you have this import
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
        // Fetch the PlannerEntity by its ID
        PlannerEntity planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner not found with id: " + plannerId));

        // Use the updated repository method to find spots for the given planner
        return plannerSpotRepository.findByPlanner(planner);
    }

    // Get a specific spot by its ID
    public PlannerSpotEntity getSpotById(int plannerSpotId) {
        return plannerSpotRepository.findById(plannerSpotId)
                .orElseThrow(() -> new RuntimeException("Spot not found with id: " + plannerSpotId));
    }

    // Add a new spot to the planner
    public PlannerSpotEntity addSpot(int plannerId, String spotName, int visitOrder, int routeId) {
        // Find the planner by plannerId
        PlannerEntity planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner not found with id: " + plannerId));

        PlannerSpotEntity newSpot = new PlannerSpotEntity();
        newSpot.setPlanner(planner);  // Set the Planner entity
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
