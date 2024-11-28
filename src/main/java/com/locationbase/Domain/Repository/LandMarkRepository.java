package com.locationbase.Domain.Repository;

import com.locationbase.Entity.LandMarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandMarkRepository extends JpaRepository<LandMarkEntity, Integer> {

    List<LandMarkEntity> findByLandmarkName(String landmarkName);

    // Optionally, add queries for other criteria (e.g., based on coordinates)
    List<LandMarkEntity> findByLongitudeAndLatitude(String longitude, String latitude);
}

