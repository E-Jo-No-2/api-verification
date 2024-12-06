package com.locationbase.Domain.repository;

import com.locationbase.entity.MemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<MemoEntity, Integer> {

   //메모 repository 추가부분
    List<MemoEntity> findByPlanner_PlannerId(int plannerId);
}
