package com.locationbase.domain.repository;

import com.locationbase.entity.PlannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PlannerRepository extends JpaRepository<PlannerEntity, Integer> {

    Optional<PlannerEntity> findByUserUserIdAndDate(String userId, LocalDate date);
}
