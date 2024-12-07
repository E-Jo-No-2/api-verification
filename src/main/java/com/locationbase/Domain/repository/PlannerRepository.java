package com.locationbase.Domain.repository;

import com.locationbase.entity.PlannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannerRepository extends JpaRepository<PlannerEntity, Integer> {
    // 필요하면 추가 메서드 정의
}
