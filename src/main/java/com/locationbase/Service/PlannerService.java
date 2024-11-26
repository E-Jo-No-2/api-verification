/*package com.locationbase.Service;

import com.locationbase.Entity.LandMarkEntity;
import com.locationbase.Entity.PlannerEntity;
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

    public PlannerEntity updatePlanner(Integer planner_id, Date newStart_date, String newTheme_name) {

        PlannerEntity existingPlanner = plannerRepository.findById(planner_id)
                .orElseThrow(() -> new RuntimeException("Planner not found with ID: " + planner_id));


        existingPlanner.setStart_date(newStart_date.toLocalDate());
        existingPlanner.setTheme_name(newTheme_name);

        return plannerRepository.save(existingPlanner);

    }

    public void deletePlanner(Integer planner_id) {
        // Check if the planner exists
        if (!plannerRepository.existsById(planner_id)) {
            throw new RuntimeException("Planner not found with ID: " + planner_id);
        }


    }
}*/



