package com.locationbase.Domain.repository;

import com.locationbase.entity.PlannerSpotEntity; // Ensure this imports the correct entity class
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlannerSpotRepository extends JpaRepository<PlannerSpotEntity, Integer> {

    // 특정 플래너 ID로 PlannerSpot 엔티티를 조회하는 메서드
    List<PlannerSpotEntity> findByPlanner_PlannerId(int plannerId);

    // 특정 플래너 ID와 루트 ID로 최대 visit_order를 조회하는 메서드
    @Query("SELECT MAX(ps.visitOrder) FROM PlannerSpotEntity ps WHERE ps.planner.plannerId = :plannerId AND ps.route.routeId = :routeId")
    Integer findMaxVisitOrderByPlannerAndRoute(@Param("plannerId") int plannerId, @Param("routeId") int routeId);
}
