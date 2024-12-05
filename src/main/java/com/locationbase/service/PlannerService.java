package com.locationbase.service;

import com.locationbase.Domain.repository.PlannerRepository;
import com.locationbase.Domain.repository.UserRepository;
import com.locationbase.Domain.repository.WeatherRepository;
import com.locationbase.entity.PlannerEntity;
import com.locationbase.entity.UserEntity;
import com.locationbase.entity.WeatherEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    // planner 저장
    public void savePlanner(String userId, LocalDate date) {
        logger.debug("Planner 저장 시작. 사용자 ID: {}, 날짜: {}", userId, date);

        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            logger.error("사용자를 찾을 수 없습니다. 사용자 ID: {}", userId);
            throw new RuntimeException("사용자를 찾을 수 없습니다. 사용자 ID: " + userId);
        }

        UserEntity user = userOptional.get();
        logger.debug("사용자 확인 완료: {}", user);

        PlannerEntity planner = new PlannerEntity(user, date);
        plannerRepository.save(planner);

        logger.debug("Planner 저장 성공. 사용자 ID: {}, 날짜: {}", userId, date);
    }

    // planner 업데이트
    public void updatePlanner(int plannerId, String userId, LocalDate newDate) {
        logger.debug("Planner 업데이트 시작. Planner ID: {}, 사용자 ID: {}, 새로운 날짜: {}", plannerId, userId, newDate);

        PlannerEntity planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner를 찾을 수 없습니다. Planner ID: " + plannerId));

        // 사용자 소유 확인
        if (!planner.getUser().getUserId().equals(userId)) {
            logger.error("사용자 ID가 Planner와 일치하지 않습니다. 사용자 ID: {}, Planner ID: {}", userId, plannerId);
            throw new RuntimeException("사용자 ID가 Planner와 일치하지 않습니다.");
        }

        // 날짜 업데이트
        planner.setDate(newDate);

        plannerRepository.save(planner);

        logger.debug("Planner 업데이트 성공. Planner ID: {}, 새로운 날짜: {}", plannerId, newDate);
    }

    // planner 삭제
    public void deletePlanner(int plannerId) {
        logger.debug("Planner 삭제 시작. Planner ID: {}", plannerId);

        if (!plannerRepository.existsById(plannerId)) {
            logger.error("Planner를 찾을 수 없습니다. Planner ID: {}", plannerId);
            throw new RuntimeException("Planner를 찾을 수 없습니다. Planner ID: " + plannerId);
        }

        plannerRepository.deleteById(plannerId);

        logger.debug("Planner 삭제 성공. Planner ID: {}", plannerId);
    }
}
