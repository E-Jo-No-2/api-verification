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
}*/
package com.locationbase.service;
import com.locationbase.Domain.repository.PlannerRepository;
import com.locationbase.Domain.repository.UserRepository;
import com.locationbase.Domain.repository.WeatherRepository;

import com.locationbase.entity.PlannerEntity;
import com.locationbase.entity.UserEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PlannerService {

    private static final Logger logger = LoggerFactory.getLogger(PlannerService.class);

    private final PlannerRepository plannerRepository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public PlannerService(PlannerRepository plannerRepository, UserRepository userRepository, JdbcTemplate jdbcTemplate) {
        this.plannerRepository = plannerRepository;
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;

    }

    // planner 저장
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


        PlannerEntity planner = new PlannerEntity();
        planner.setPlannerId(plannerId);
        planner.setUserId(user);
        planner.setDate(currentDate);


        plannerRepository.save(planner);

        logger.debug("Planner 저장 성공. 사용자 ID: {}", userId);
    }

    //planner 업데이트
    public void updatePlanner(int plannerId, String userId, LocalDate newDate) {
        logger.debug("Planner 업데이트 시작. Planner ID: {}, 사용자 ID: {}", plannerId, userId);

        PlannerEntity planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner를 찾을 수 없습니다. Planner ID: " + plannerId));


        if (!planner.getUserId().getUserId().equals(userId)) {
            logger.error("사용자 ID가 Planner와 일치하지 않습니다. 사용자 ID: {}, Planner ID: {}", userId, plannerId);
            throw new RuntimeException("사용자 ID가 Planner와 일치하지 않습니다.");
        }


        planner.setDate(newDate);


        plannerRepository.save(planner);

        logger.debug("Planner 업데이트 성공. Planner ID: {}, 새로운 날짜: {}", plannerId, newDate);
    }

    //planner 삭제c
    public void deletePlanner(int plannerId) {
        logger.debug("Planner 삭제 시작. Planner ID: {}", plannerId);

        if (!plannerRepository.existsById(plannerId)) {
            logger.error("Planner를 찾을 수 없습니다. Planner ID: {}", plannerId);
            throw new RuntimeException("Planner를 찾을 수 없습니다. Planner ID: " + plannerId);
        }
        // 날씨 정보도 함께 삭제하려면, 날씨와 관련된 로직을 추가할 수 있음
        PlannerEntity planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner를 찾을 수 없습니다. Planner ID: " + plannerId));

        // WeatherEntity를 따로 삭제할 필요는 없지만, 삭제하려면 따로 처리 필요

            // WeatherEntity 삭제 로직 (필요시)

            plannerRepository.deleteById(plannerId);

            logger.debug("Planner 삭제 성공. Planner ID: {}", plannerId);
        }

    public void deletePlannerAndResetAutoIncrement(int plannerId) {
        logger.debug("Planner 삭제 시작. Planner ID: {}", plannerId);

        if (!plannerRepository.existsById(plannerId)) {
            logger.error("Planner를 찾을 수 없습니다. Planner ID: {}", plannerId);
            throw new RuntimeException("Planner를 찾을 수 없습니다. Planner ID: " + plannerId);
        }

        plannerRepository.deleteById(plannerId);
        logger.debug("Planner 삭제 성공. Planner ID: {}", plannerId);

        // Planner 테이블이 비어 있는 경우만 AUTO_INCREMENT 초기화
        resetAutoIncrement();
    }

    private void resetAutoIncrement() {
        String truncateCheckSql = "SELECT COUNT(*) FROM planner";
        Integer count = jdbcTemplate.queryForObject(truncateCheckSql, Integer.class);

        if (count != null && count > 0) {
            logger.error("Planner 테이블이 비어 있지 않으므로 AUTO_INCREMENT를 초기화할 수 없습니다.");
            throw new RuntimeException("Planner 테이블이 비어 있지 않습니다. 초기화하려면 모든 데이터를 삭제해야 합니다.");
        }

        String resetSql = "ALTER TABLE planner AUTO_INCREMENT = 1";
        try {
            jdbcTemplate.execute(resetSql);
            logger.debug("Planner 테이블 AUTO_INCREMENT 초기화 성공");
        } catch (Exception e) {
            logger.error("Planner 테이블 AUTO_INCREMENT 초기화 실패", e);
            throw new RuntimeException("AUTO_INCREMENT 초기화 중 오류 발생", e);
        }
    }


}

