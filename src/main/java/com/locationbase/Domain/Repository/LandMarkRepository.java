package com.locationbase.Domain.Repository;

import com.locationbase.Entity.LandMarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandMarkRepository extends JpaRepository<LandMarkEntity, Integer> {
    @Query("SELECT l FROM LandMarkEntity l WHERE l.theme_name = :theme_name")
    List<LandMarkEntity> findByThemeName(@Param("theme_name") String theme_name);

}

