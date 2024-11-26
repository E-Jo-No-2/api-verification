package com.locationbase.Domain.Repository;

import com.locationbase.Entity.SpotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpotRepository extends JpaRepository<SpotEntity, Integer> {}