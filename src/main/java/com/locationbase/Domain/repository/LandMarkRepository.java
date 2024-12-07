package com.locationbase.Domain.repository;

import com.locationbase.entity.LandMarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandMarkRepository extends JpaRepository<LandMarkEntity, Integer> {



}
