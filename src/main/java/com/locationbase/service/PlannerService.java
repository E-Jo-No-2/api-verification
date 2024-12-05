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
        logger.debug("현재 날짜: {}", currentDate);

        PlannerEntity planner = new PlannerEntity();
        planner.setPlannerId(plannerId);
        planner.setUserId(user);
        planner.setDate(currentDate);

        plannerRepository.save(planner);
        logger.debug("Planner 저장 성공. 사용자 ID: {}", userId);
    }

    // planner 업데이트
    public void updatePlanner(int plannerId, String userId, LocalDate newDate) {
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
    }

    // planner 삭제
    public void deletePlanner(int plannerId) {
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

    private void resetAutoIncrement() {
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
}
