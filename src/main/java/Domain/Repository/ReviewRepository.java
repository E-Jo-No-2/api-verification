package Domain.Repository;

import Domain.Entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {

    // user_id로 리뷰조회
    List<ReviewEntity> findByUserId(String user_id);

    // spot_id로 리뷰조회
    List<ReviewEntity> findBySpotId(int spot_id);

    // spot_id와 user_id를 통해 리뷰조회
    List<ReviewEntity> findByUserIdAndSpotId(String user_id, int spot_id);
}
