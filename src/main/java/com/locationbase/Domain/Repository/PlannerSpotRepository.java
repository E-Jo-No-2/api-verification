package com.locationbase.Domain.Repository;

import com.locationbase.Entity.PlannerSpotEntity;
import com.locationbase.Entity.PlannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerSpotRepository extends JpaRepository<PlannerSpotEntity, Integer> {

    // Find spots by the PlannerEntity object, not the plannerId
    List<PlannerSpotEntity> findByPlanner(PlannerEntity planner);
}
