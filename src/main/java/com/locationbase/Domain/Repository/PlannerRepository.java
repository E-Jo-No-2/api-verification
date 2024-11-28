package com.locationbase.Domain.Repository;

import com.locationbase.Entity.PlannerEntity;
import com.locationbase.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlannerRepository extends JpaRepository<PlannerEntity, Integer> {



}
