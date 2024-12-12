package com.locationbase.domain.repository;

import com.locationbase.entity.PlannerSpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlannerSpotRepository extends JpaRepository<PlannerSpotEntity, Integer> {

    // 플래너 ID에 따른 최대 방문 순서 조회
    @Query("SELECT MAX(ps.visitOrder) FROM PlannerSpotEntity ps WHERE ps.planner.plannerId = :plannerId")
    Optional<Integer> findMaxVisitOrderByPlannerId(int plannerId);
}
