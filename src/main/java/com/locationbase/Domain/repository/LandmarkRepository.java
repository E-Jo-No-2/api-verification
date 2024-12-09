package com.locationbase.domain.repository;

import com.locationbase.entity.LandmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LandmarkRepository extends JpaRepository<LandmarkEntity, Integer> {
    Optional<LandmarkEntity> findByLandmarkName(String landmarkName);
}
