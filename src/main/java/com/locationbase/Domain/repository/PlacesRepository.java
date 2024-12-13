package com.locationbase.Domain.repository;

import com.locationbase.entity.PlacesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlacesRepository extends JpaRepository<PlacesEntity, Integer> {
    @Query("SELECT p FROM PlacesEntity p WHERE p.name = :name")
    java.util.Optional<PlacesEntity> findByName(@Param("name") String name);

    Optional<PlacesEntity> findByNameAndLatAndLng(String name, String lat, String lng);

}
