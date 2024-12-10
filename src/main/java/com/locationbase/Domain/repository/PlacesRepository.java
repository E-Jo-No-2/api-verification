package com.locationbase.domain.repository;

import com.locationbase.entity.PlacesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlacesRepository extends JpaRepository<PlacesEntity, Integer> {
    @Query("SELECT AVG(r.rating) AS averageRating, COUNT(r) AS reviewCount " +
            "FROM ReviewEntity r WHERE r.place.placeId = ?1")
    Object[] getAverageRatingByPlaceId(int placeId);

    Optional<PlacesEntity> findByName(String name);

    Optional<PlacesEntity> findByNameAndLatAndLng(String name, String lat, String lng);
}
