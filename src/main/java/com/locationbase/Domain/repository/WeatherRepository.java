package com.locationbase.domain.repository;

import com.locationbase.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, LocalDate> {
}
