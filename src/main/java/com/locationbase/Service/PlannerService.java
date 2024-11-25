package com.locationbase.Service;

import Domain.Entity.LandMarkEntity;
import Domain.Entity.PlannerEntity;
import com.locationbase.Domain.Repository.LandMarkRepository;
import com.locationbase.Domain.Repository.PlannerRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class PlannerService {
    private final PlannerRepository plannerRepository;
    private final LandMarkRepository landmarkRepository;

    public PlannerService(PlannerRepository plannerRepository, LandMarkRepository landmarkRepository) {
        this.plannerRepository = plannerRepository;
        this.landmarkRepository = landmarkRepository;
    }

    public PlannerEntity createPlanner(Date start_date, String theme_name) {
        PlannerEntity planner = new PlannerEntity();
        planner.setStart_date(start_date.toLocalDate());
        planner.setTheme_name(theme_name);

        return plannerRepository.save(planner);
    }

    public List<LandMarkEntity> getLandmarksByTheme(String theme_name) {
        return landmarkRepository.findByTheme_name(theme_name);


    }

    public PlannerEntity updatePlanner(Integer plannerId, Date newStart_date, String newTheme_name) {
        // Retrieve the existing planner or throw an exception if it doesn't exist
        PlannerEntity existingPlanner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner not found with ID: " + plannerId));

        // Update the fields with new values
        existingPlanner.setStart_date(newStart_date.toLocalDate());
        existingPlanner.setTheme_name(newTheme_name);

        return plannerRepository.save(existingPlanner);

    }

    public void deletePlanner(Integer plannerId) {
        // Check if the planner exists
        if (!plannerRepository.existsById(plannerId)) {
            throw new RuntimeException("Planner not found with ID: " + plannerId);
        }


    }
}



