package com.locationbase.Domain.repository;

import com.locationbase.entity.PlacesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<PlacesEntity, Integer> {
}
