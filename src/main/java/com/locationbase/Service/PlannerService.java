/*package com.locationbase.Service;

import com.locationbase.Domain.Repository.PlannerRepository;
import com.locationbase.Domain.Repository.UserRepository;
import com.locationbase.Domain.Repository.WeatherRepository;
import com.locationbase.Entity.PlannerEntity;
import com.locationbase.Entity.UserEntity;
import com.locationbase.Entity.WeatherEntity;
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

    public void savePlanner(int plannerId, String userId) {
        logger.debug("Starting savePlanner for userId: {}", userId);

        Optional<UserEntity> userOptional = userRepository.findById(userId);
        logger.debug("User query executed, result: {}", userOptional);

        if (!userOptional.isPresent()) {
            logger.error("User not found with userId: {}", userId);
            throw new RuntimeException("User not found with userId: " + userId);
        }

        UserEntity user = userOptional.get();
        logger.debug("User found: {}", user);

        LocalDate currentDate = LocalDate.now();
        WeatherEntity weather = weatherRepository.findById(currentDate)
                .orElseThrow(() -> new RuntimeException("Weather not found for today"));

        PlannerEntity planner = new PlannerEntity();
        planner.setPlannerId(plannerId);
        planner.setUserId(user);
        planner.setDate(currentDate);

        plannerRepository.save(planner);

        logger.debug("Planner saved successfully for userId: {}", userId);
    }
}*/

package com.locationbase.Service;

import com.locationbase.Domain.Repository.PlannerRepository;
import com.locationbase.Domain.Repository.UserRepository;
import com.locationbase.Domain.Repository.WeatherRepository;
import com.locationbase.Entity.PlannerEntity;
import com.locationbase.Entity.UserEntity;
import com.locationbase.Entity.WeatherEntity;
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

    public void savePlanner(int plannerId, String userId) {
        logger.debug("Planner 저장 시작. 사용자 ID: {}", userId);

        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            logger.error("사용자를 찾을 수 없습니다. 사용자 ID: {}", userId);
            throw new RuntimeException("사용자를 찾을 수 없습니다. 사용자 ID: " + userId);
        }

        UserEntity user = userOptional.get();
        logger.debug("사용자 확인 완료: {}", user);

        LocalDate currentDate = LocalDate.now();
        WeatherEntity weather = weatherRepository.findById(currentDate)
                .orElseThrow(() -> new RuntimeException("오늘 날짜의 날씨 데이터를 찾을 수 없습니다."));

        PlannerEntity planner = new PlannerEntity();
        planner.setPlannerId(plannerId);
        planner.setUserId(user);
        planner.setDate(currentDate);

        plannerRepository.save(planner);

        logger.debug("Planner 저장 성공. 사용자 ID: {}", userId);
    }
}
