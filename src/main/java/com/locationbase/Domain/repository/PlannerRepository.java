package com.locationbase.domain.repository;

import com.locationbase.entity.PlannerEntity;
import com.locationbase.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlannerRepository extends JpaRepository<PlannerEntity, Integer> {


    List<PlannerEntity> findByUserId(UserEntity user);

    boolean existsByUserIdAndDate(UserEntity user, LocalDate date);
}



