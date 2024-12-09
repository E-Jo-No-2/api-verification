package com.locationbase.service;

import com.locationbase.domain.repository.PlannerRepository;
import com.locationbase.domain.repository.UserRepository;
import com.locationbase.entity.PlannerEntity;
import com.locationbase.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class PlannerService {

    private static final Logger logger = LoggerFactory.getLogger(PlannerService.class);

    private final PlannerRepository plannerRepository;
    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    @Autowired
    public PlannerService(PlannerRepository plannerRepository,
                          UserRepository userRepository,
                          JdbcTemplate jdbcTemplate,
                          TransactionTemplate transactionTemplate) {
        this.plannerRepository = plannerRepository;
        this.userRepository = userRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    // planner 저장
    public int savePlanner(String userId) {
        logger.debug("Planner 저장 시작. 사용자 ID: {}", userId);

        // 사용자 ID로 UserEntity 조회 (findByUserId 사용)
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. 사용자 ID: " + userId));

        logger.debug("사용자 확인 완료: {}", user);

        // 현재 날짜를 사용하여 PlannerEntity 생성
        LocalDate currentDate = LocalDate.now();
        logger.debug("현재 날짜: {}", currentDate);

        PlannerEntity planner = new PlannerEntity();
        planner.setUser(user);  // UserEntity 설정
        planner.setDate(currentDate);  // 날짜 설정

        plannerRepository.save(planner);  // 저장
        logger.debug("Planner 저장 성공. 사용자 ID: {}", userId);
        return planner.getPlannerId();  // 생성된 plannerId 반환
    }

    // planner 업데이트
    public void updatePlanner(int plannerId, String userId, LocalDate newDate) {
        logger.debug("Planner 업데이트 시작. Planner ID: {}, 사용자 ID: {}", plannerId, userId);

        PlannerEntity planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner를 찾을 수 없습니다. Planner ID: " + plannerId));

        logger.debug("Planner 확인 완료. Planner ID: {}, 사용자 ID: {}", plannerId, userId);

        if (!planner.getUser().getUserId().equals(userId)) {
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

        // 트랜잭션 내 삭제 및 planner_id 재정렬
        plannerRepository.deleteById(plannerId);
        logger.debug("Planner 삭제 완료. Planner ID: {}", plannerId);

        String reSeqSql = "UPDATE planner SET planner_id = planner_id - 1 WHERE planner_id > ?";
        jdbcTemplate.update(reSeqSql, plannerId);
        logger.debug("planner_id 재정렬 완료. Planner ID: {}", plannerId);

        // 별도의 트랜잭션에서 AUTO_INCREMENT 값 재설정
        resetAutoIncrement();
    }

    private void resetAutoIncrement() {
        transactionTemplate.executeWithoutResult(status -> {
            try {
                // 최신 planner_id 가져오기
                String maxIdSql = "SELECT MAX(planner_id) FROM planner";
                Integer maxId = jdbcTemplate.queryForObject(maxIdSql, Integer.class);

                if (maxId != null) {
                    // AUTO_INCREMENT를 다음 값으로 재설정
                    String resetSql = "ALTER TABLE planner AUTO_INCREMENT = ?";
                    jdbcTemplate.update(resetSql, maxId + 1);
                    logger.debug("AUTO_INCREMENT 값을 {}로 재설정", maxId + 1);
                } else {
                    // 테이블이 비어 있는 경우 AUTO_INCREMENT를 1로 설정
                    String resetSql = "ALTER TABLE planner AUTO_INCREMENT = 1";
                    jdbcTemplate.update(resetSql);
                    logger.debug("테이블이 비어 있으므로 AUTO_INCREMENT를 1로 재설정");
                }
            } catch (Exception e) {
                logger.error("AUTO_INCREMENT 재설정 중 오류 발생", e);
                throw new RuntimeException("AUTO_INCREMENT 재설정 실패", e);
            }
        });
    }
}
