package com.locationbase.Service;

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
}
