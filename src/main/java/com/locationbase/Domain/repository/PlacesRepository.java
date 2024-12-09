package com.locationbase.domain.repository;

import com.locationbase.entity.PlacesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlacesRepository extends JpaRepository<PlacesEntity, Integer> {
}
