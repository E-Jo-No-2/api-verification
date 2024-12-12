package com.locationbase.Domain.repository;

import com.locationbase.entity.LandMarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandMarkRepository extends JpaRepository<LandMarkEntity, Integer> {



}
