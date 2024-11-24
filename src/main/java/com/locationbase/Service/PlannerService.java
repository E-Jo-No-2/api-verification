package com.locationbase.Service;

import Domain.Entity.LandMarkEntity;
import Domain.Entity.PlannerEntity;
import Domain.Entity.ThemeEntity;
import com.locationbase.DTO.PlannerDTO;
import com.locationbase.Domain.Repository.LandMarkRepository;
import com.locationbase.Domain.Repository.PlannerRepository;
import com.locationbase.Domain.Repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlannerService {

    @Autowired
    private PlannerRepository plannerRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private LandMarkRepository landmarkRepository;


    public PlannerDTO createPlanner(PlannerDTO plannerDTO) {
        ThemeEntity theme = themeRepository.findById(plannerDTO.getTheme_name())
                .orElseThrow(() -> new IllegalArgumentException("Theme not found"));

        LandMarkEntity landmark = landmarkRepository.findById(plannerDTO.getLandmark_id())
                .orElseThrow(() -> new IllegalArgumentException("Landmark not found"));

        PlannerEntity plannerEntity = new PlannerEntity();
        plannerEntity.setUser_id(plannerDTO.getUser_id());
        plannerEntity.setStart_date(plannerDTO.getStart_date());
        plannerEntity.setTheme_name(theme.getThemeName());

        PlannerEntity savedPlanner = plannerRepository.save(plannerEntity);

        PlannerDTO savedPlannerDTO = new PlannerDTO();
        savedPlannerDTO.setPlanner_id(savedPlanner.getPlanner_id());
        savedPlannerDTO.setUser_id(savedPlanner.getUser_id());
        savedPlannerDTO.setStart_date(savedPlanner.getStart_date());
        savedPlannerDTO.setTheme_name(savedPlanner.getTheme_name());
        savedPlannerDTO.setLandmark_id(plannerDTO.getLandmark_id());
        return savedPlannerDTO;
    }


    public PlannerDTO updatePlanner(int planner_id, PlannerDTO plannerDTO) {


        PlannerEntity plannerEntity = plannerRepository.findById(planner_id)
                .orElseThrow(() -> new IllegalArgumentException("Planner not found with ID: " + planner_id));


        if (plannerDTO.getTheme_name() != null) {
            ThemeEntity theme = themeRepository.findById(plannerDTO.getTheme_name())
                    .orElseThrow(() -> new IllegalArgumentException("Theme not found"));
            plannerEntity.setTheme_name(theme.getThemeName());
        }

        if (plannerDTO.getLandmark_id() != 0) {
            LandMarkEntity landmark = landmarkRepository.findById(plannerDTO.getLandmark_id())
                    .orElseThrow(() -> new IllegalArgumentException("Landmark not found"));
        }


        if (plannerDTO.getStart_date() != null) {
            plannerEntity.setStart_date(plannerDTO.getStart_date());
        }

        PlannerEntity updatedPlanner = plannerRepository.save(plannerEntity);

        PlannerDTO updatedPlannerDTO = new PlannerDTO();
        updatedPlannerDTO.setPlanner_id(updatedPlanner.getPlanner_id());
        updatedPlannerDTO.setUser_id(updatedPlanner.getUser_id());
        updatedPlannerDTO.setStart_date(updatedPlanner.getStart_date());
        updatedPlannerDTO.setTheme_name(updatedPlanner.getTheme_name());
        return updatedPlannerDTO;
    }

    public void deletePlanner(int planner_id) {
        if (!plannerRepository.existsById(planner_id)) {
            throw new IllegalArgumentException("Planner not found with ID: " + planner_id);
        }
        plannerRepository.deleteById(planner_id);
    }
}
