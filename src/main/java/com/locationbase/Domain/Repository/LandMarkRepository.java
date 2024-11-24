package com.locationbase.Domain.Repository;

import Domain.Entity.LandMarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LandMarkRepository extends JpaRepository<LandMarkEntity, Integer> {
}
