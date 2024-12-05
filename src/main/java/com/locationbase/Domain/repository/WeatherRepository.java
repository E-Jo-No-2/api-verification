package com.locationbase.Domain.repository;

import com.locationbase.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, LocalDate> {
    // 필요시 추가 메서드 정의
}
