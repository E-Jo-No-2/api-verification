package com.locationbase.domain.repository;

import com.locationbase.dto.ReviewDTO;
import com.locationbase.entity.PlacesEntity;
import com.locationbase.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

    // 장소 ID를 기반으로 평균 평점과 리뷰 수 반환
    @Query("SELECT AVG(r.rating) AS averageRating, COUNT(r) AS reviewCount " +
            "FROM ReviewEntity r WHERE r.place.placeId = :placeId")
    List<ReviewDTO> getReviewsByPlaceId(@Param("placeId") int placeId);
    //Object[] getReviewsByPlaceId(@Param("placeId") int placeId);
}
