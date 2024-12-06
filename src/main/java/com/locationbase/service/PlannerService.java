package com.locationbase.service;

import com.locationbase.domain.repository.PlannerRepository;
import com.locationbase.domain.repository.UserRepository;
import com.locationbase.domain.repository.WeatherRepository;
import com.locationbase.entity.PlannerEntity;
import com.locationbase.entity.UserEntity;
import com.locationbase.entity.WeatherEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PlannerService {

    private static final Logger logger = LoggerFactory.getLogger(PlannerService.class);

    private final PlannerRepository plannerRepository;
    private final UserRepository userRepository;
    private final WeatherRepository weatherRepository;

    @Autowired
    public PlannerService(PlannerRepository plannerRepository, UserRepository userRepository, WeatherRepository weatherRepository) {
        this.plannerRepository = plannerRepository;
        this.userRepository = userRepository;
        this.weatherRepository = weatherRepository;
    }

    // 사용자 검증 메소드 (중복 제거)
    private UserEntity validateUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("사용자를 찾을 수 없습니다. 사용자 ID: {}", userId);
                    return new RuntimeException("사용자를 찾을 수 없습니다. 사용자 ID: " + userId);
                });
    }

    // Planner 저장
    public PlannerEntity savePlanner(String userId, LocalDate date) {
        logger.debug("Planner 저장 시작. 사용자 ID: {}, 날짜: {}", userId, date);

        UserEntity user = validateUser(userId);
        logger.debug("사용자 확인 완료: {}", user);

        PlannerEntity planner = new PlannerEntity(user, date);

        // 날씨 정보 추가 처리
        Optional<WeatherEntity> weatherOptional = weatherRepository.findByDate(date);
        if (weatherOptional.isPresent()) {
            planner.setWeather(weatherOptional.get());
            logger.debug("날씨 데이터 추가 완료: {}", weatherOptional.get());
        } else {
            logger.warn("날씨 데이터를 찾을 수 없습니다. 날짜: {}", date);
        }

        plannerRepository.save(planner);

        logger.debug("Planner 저장 성공. 사용자 ID: {}, 날짜: {}", userId, date);
        return planner;
    }

    // Planner 삭제
    public void deletePlanner(int plannerId) {
        logger.debug("Planner 삭제 시작. Planner ID: {}", plannerId);

        if (!plannerRepository.existsById(plannerId)) {
            logger.error("Planner를 찾을 수 없습니다. Planner ID: {}", plannerId);
            throw new RuntimeException("Planner를 찾을 수 없습니다. Planner ID: " + plannerId);
        }

        plannerRepository.deleteById(plannerId);

        logger.debug("Planner 삭제 성공. Planner ID: {}", plannerId);
    }

    // Planner ID로 플래너 조회
    public PlannerEntity getPlannerById(int plannerId) {
        logger.debug("Planner 조회 시작. Planner ID: {}", plannerId);
        return plannerRepository.findById(plannerId)
                .orElseThrow(() -> {
                    logger.error("Planner를 찾을 수 없습니다. Planner ID: {}", plannerId);
                    return new RuntimeException("Planner를 찾을 수 없습니다. Planner ID: " + plannerId);
                });
    }

}
