package com.locationbase.Domain.repository;

import com.locationbase.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

    @Query("SELECT r FROM ReviewEntity r JOIN FETCH r.place p WHERE p.placeId = :placeId")
    List<ReviewEntity> findByPlaceId(@Param("placeId") int placeId);
}

