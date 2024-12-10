package com.locationbase.domain.repository;

import com.locationbase.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

    // 장소 ID를 기반으로 평균 평점과 리뷰 수 반환
    @Query("SELECT AVG(r.rating) AS averageRating, COUNT(r) AS reviewCount " +
            "FROM ReviewEntity r WHERE r.place.placeId = ?1")
    Object[] getAverageRatingByPlaceId(int placeId);

}
