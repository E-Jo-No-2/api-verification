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
    public void savePlanner(int plannerId, String userId) {
        logger.debug("Planner 저장 시작. 사용자 ID: {}", userId);

        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            logger.error("사용자를 찾을 수 없습니다. 사용자 ID: {}", userId);
            throw new RuntimeException("사용자를 찾을 수 없습니다. 사용자 ID: " + userId);
        }
        // 사용자 검증 메소드 (중복 제거)
        private UserEntity validateUser (String userId){
            return userRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.error("사용자를 찾을 수 없습니다. 사용자 ID: {}", userId);
                        return new RuntimeException("사용자를 찾을 수 없습니다. 사용자 ID: " + userId);
                    });
        }

        // Planner 저장
        public PlannerEntity savePlanner (String userId, LocalDate date){
            logger.debug("Planner 저장 시작. 사용자 ID: {}, 날짜: {}", userId, date);

            UserEntity user = validateUser(userId);
            logger.debug("사용자 확인 완료: {}", user);

            LocalDate currentDate = LocalDate.now();
            logger.debug("현재 날짜: {}", currentDate);

            PlannerEntity planner = new PlannerEntity();
            planner.setPlannerId(plannerId);
            planner.setUserId(user);
            planner.setDate(currentDate);

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
            logger.debug("Planner 저장 성공. 사용자 ID: {}", userId);
        }

        // planner 업데이트
        public void updatePlanner ( int plannerId, String userId, LocalDate newDate){
            logger.debug("Planner 업데이트 시작. Planner ID: {}, 사용자 ID: {}", plannerId, userId);

            PlannerEntity planner = plannerRepository.findById(plannerId)
                    .orElseThrow(() -> new RuntimeException("Planner를 찾을 수 없습니다. Planner ID: " + plannerId));

            logger.debug("Planner 확인 완료. Planner ID: {}, 사용자 ID: {}", plannerId, userId);

            if (!planner.getUserId().getUserId().equals(userId)) {
                logger.error("사용자 ID가 Planner와 일치하지 않습니다. 사용자 ID: {}, Planner ID: {}", userId, plannerId);
                throw new RuntimeException("사용자 ID가 Planner와 일치하지 않습니다.");
            }

            planner.setDate(newDate);
            plannerRepository.save(planner);
            logger.debug("Planner 업데이트 성공. Planner ID: {}, 새로운 날짜: {}", plannerId, newDate);

            logger.debug("Planner 저장 성공. 사용자 ID: {}, 날짜: {}", userId, date);
            return planner;
        }

        // Planner 삭제
        public void deletePlanner ( int plannerId){
            logger.debug("Planner 삭제 시작. Planner ID: {}", plannerId);

            if (!plannerRepository.existsById(plannerId)) {
                logger.error("Planner를 찾을 수 없습니다. Planner ID: {}", plannerId);
                throw new RuntimeException("Planner를 찾을 수 없습니다. Planner ID: " + plannerId);
            }

            plannerRepository.deleteById(plannerId);
            logger.debug("Planner 삭제 완료. Planner ID: {}", plannerId);

            // planner_id를 재정렬하는 SQL 실행
            String reSeqSql = "UPDATE planner SET planner_id = planner_id - 1 WHERE planner_id > ?";
            jdbcTemplate.update(reSeqSql, plannerId);
            logger.debug("planner_id 재정렬 완료. Planner ID: {}", plannerId);

            // AUTO_INCREMENT 값을 재설정
            resetAutoIncrement();
        }

        private void resetAutoIncrement () {
            // 최신 planner_id를 가져오기
            String maxIdSql = "SELECT MAX(planner_id) FROM planner";
            Integer maxId = jdbcTemplate.queryForObject(maxIdSql, Integer.class);

            if (maxId != null) {
                // AUTO_INCREMENT를 다음 ID로 설정
                String resetSql = "ALTER TABLE planner AUTO_INCREMENT = ?";
                jdbcTemplate.update(resetSql, maxId + 1);  // 다음 값으로 설정
                logger.debug("AUTO_INCREMENT 값을 {}로 재설정", maxId + 1);
            } else {
                // 테이블이 비어 있으면 1로 설정
                String resetSql = "ALTER TABLE planner AUTO_INCREMENT = 1";
                jdbcTemplate.update(resetSql);
                logger.debug("테이블이 비어 있으므로 AUTO_INCREMENT를 1로 재설정");
            }
        }

        // Planner ID로 플래너 조회
        public PlannerEntity getPlannerById ( int plannerId){
            logger.debug("Planner 조회 시작. Planner ID: {}", plannerId);
            return plannerRepository.findById(plannerId)
                    .orElseThrow(() -> {
                        logger.error("Planner를 찾을 수 없습니다. Planner ID: {}", plannerId);
                        return new RuntimeException("Planner를 찾을 수 없습니다. Planner ID: " + plannerId);
                    });
        }

    }
}