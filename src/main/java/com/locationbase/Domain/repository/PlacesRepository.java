package com.locationbase.domain.repository;

import com.locationbase.entity.PlacesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlacesRepository extends JpaRepository<PlacesEntity, Integer> {
    Optional<PlacesEntity> findByNameAndLatAndLng(String name, String lat, String lng); // 중복 체크용 메서드

}
