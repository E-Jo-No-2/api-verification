package com.locationbase.Domain.repository;

import com.locationbase.entity.LandmarkEntity;
import com.locationbase.entity.LandmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LandMarkRepository extends JpaRepository<LandmarkEntity, Integer> {
    Optional<Object> findByLandmarkName(String landmarkName);


    //Optional<Object> findByLandMarkName(String landmarkName);
}
