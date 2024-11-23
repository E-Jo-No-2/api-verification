package com.locationbase.Domain.Repository;


import com.locationbase.Domain.Entity.PlannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlannerRepository extends JpaRepository<PlannerEntity, Integer> {

    // 사용자 ID로 플래너 검색
    List<PlannerEntity> findByUserId(String userId);

    // 테마 이름으로 플래너 검색
    List<PlannerEntity> findByThemeName(String themeName);

    // 특정날짜로 플래너 검색
    List<PlannerEntity> findByStartDateAfter(LocalDate startDate);

    // 특정 기간내 플래너 검색
    List<PlannerEntity> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
