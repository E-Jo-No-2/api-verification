package com.locationbase.Domain.Repository;

import com.locationbase.Entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, LocalDate> {


}
