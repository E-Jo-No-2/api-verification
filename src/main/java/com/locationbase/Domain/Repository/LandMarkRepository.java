package com.locationbase.Domain.Repository;

import Domain.Entity.LandMarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandMarkRepository extends JpaRepository<LandMarkEntity, Integer> {

    List<LandMarkEntity> findByTheme_name(String themeName);
}
