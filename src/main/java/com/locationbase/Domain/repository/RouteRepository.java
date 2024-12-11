package com.locationbase.domain.repository;

import com.locationbase.entity.RouteEntity;
import com.locationbase.entity.PlacesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<RouteEntity, Integer> {
    boolean existsByStartPointAndEndPoint(PlacesEntity startPoint, PlacesEntity endPoint);

    // 특정 시작 지점과 종료 지점의 경로를 조회하는 메서드 추가
    Optional<RouteEntity> findByStartPointAndEndPoint(PlacesEntity startPoint, PlacesEntity endPoint);
}
