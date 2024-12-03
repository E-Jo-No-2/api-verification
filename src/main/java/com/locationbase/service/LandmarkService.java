package com.locationbase.service;

import com.locationbase.entity.LandMarkEntity;
import com.locationbase.Domain.repository.LandMarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LandmarkService {

    private final LandMarkRepository landMarkRepository;

    @Autowired
    public LandmarkService(LandMarkRepository landMarkRepository) {
        this.landMarkRepository = landMarkRepository;
    }

    // 랜드마크와 관련된 데이터를 처리하는 로직 추가 가능
    public String getLandmarkInfo() {
        return "Landmark information loaded successfully.";
    }

    // 랜드마크 이름으로 랜드마크 엔티티를 조회하는 메서드
    public LandMarkEntity findByName(String landmarkName) {
        Optional<LandMarkEntity> landmarkOptional = landMarkRepository.findByLandmarkName(landmarkName).stream().findFirst();
        return landmarkOptional.orElse(null);
    }
}
