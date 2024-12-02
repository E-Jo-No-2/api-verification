package com.locationbase.Domain.Repository;

import com.locationbase.Entity.PlannerSpotEntity; // Ensure this imports the correct entity class
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlannerSpotRepository extends JpaRepository<PlannerSpotEntity, Integer> {
    List<PlannerSpotEntity> findByPlanner_PlannerIdOrderByVisitOrder(Integer plannerId);

    List<PlannerSpotEntity> findByPlannerId(int plannerId);
}



