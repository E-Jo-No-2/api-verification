package com.locationbase.Domain.Repository;


import com.locationbase.Domain.Entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {



    // user_id로 리뷰 찾기
    List<ReviewEntity> findByUserId(String user_id);

    //spot_id로 라뷰 찾기
    List<ReviewEntity> findBySpotId(int spot_id);


}
