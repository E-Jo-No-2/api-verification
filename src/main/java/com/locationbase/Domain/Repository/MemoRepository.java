/*package com.locationbase.Domain.Repository;

import com.locationbase.Entity.MemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<MemoEntity, Integer> {

    // Find memos by the planner_id (instead of the entire PlannerEntity)
    List<MemoEntity> findByPlannerId(int planner_id);
}*/
