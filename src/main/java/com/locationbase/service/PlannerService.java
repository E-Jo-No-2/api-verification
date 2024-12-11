package com.locationbase.service;

import com.locationbase.Domain.repository.PlannerRepository;
import com.locationbase.Domain.repository.UserRepository;
import com.locationbase.entity.PlannerEntity;
import com.locationbase.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service

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

        // 사용자 ID로 UserEntity 조회
        Optional<Object> userOpt = userRepository.findByUserId(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다. 사용자 ID: " + userId);
        }
        UserEntity user = (UserEntity) userOpt.get();

        logger.debug("사용자 확인 완료: {}", user);

        // 현재 날짜를 사용하여 PlannerEntity 생성
        LocalDate currentDate = LocalDate.now();
        logger.debug("현재 날짜: {}", currentDate);

        PlannerEntity planner = new PlannerEntity();
        planner.setUserId(user);  // UserEntity 설정
        planner.setDate(currentDate);  // 날짜 설정

        plannerRepository.save(planner);  // 저장
        logger.debug("Planner 저장 성공. 사용자 ID: {}", userId);
        return planner.getPlannerId();  // 생성된 plannerId 반환
    }


    // planner 업데이트
    public void updatePlanner(int plannerId, String userId, LocalDate newDate) {
        logger.debug("Planner 업데이트 시작. Planner ID: {}, 사용자 ID: {}", plannerId, userId, newDate);

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
    @Transactional
    public void deletePlanner(int plannerId) {
        logger.debug("Planner 삭제 시작. Planner ID: {}", plannerId);

        // plannerId 존재유무 확인
        if (!plannerRepository.existsById(plannerId)) {
            logger.error("Planner를 찾을 수 없습니다. Planner ID: {}", plannerId);
            throw new RuntimeException("Planner를 찾을 수 없습니다. Planner ID: " + plannerId);
        }

        //  planner_spot 삭제
        String deletePlannerSpotSql = "DELETE FROM planner_spot WHERE planner_id = ?";
        jdbcTemplate.update(deletePlannerSpotSql, plannerId);
        logger.debug("관련된 planner_spot 데이터 삭제 완료. Planner ID: {}", plannerId);

        //route 삭제
        String deleteRouteSql = "DELETE FROM route WHERE planner_id = ?";
        jdbcTemplate.update(deleteRouteSql, plannerId);
        logger.debug("관련된 route 데이터 삭제 완료. Planner ID: {}", plannerId);

       /* // places 삭제
        String deletePlacesSql = "DELETE FROM places WHERE planner_id = ?";
        jdbcTemplate.update(deletePlacesSql, plannerId);
        logger.debug("관련된 places 데이터 삭제 완료. Planner ID: {}", plannerId);*/

        // 1. planner와 관련된 memo 항목 삭제 (ON DELETE CASCADE가 제대로 작동하도록 함)
        String deleteMemoSql = "DELETE FROM memo WHERE planner_id = ?";
        jdbcTemplate.update(deleteMemoSql, plannerId);


        /*// planner_id를 재정렬하는 SQL 실행
        String reSeqSql = "UPDATE planner SET planner_id = planner_id - 1 WHERE planner_id > ?";
        jdbcTemplate.update(reSeqSql, plannerId);
        logger.debug("planner_id 재정렬 완료. Planner ID: {}", plannerId);*/

        // planner 삭제
       /* plannerRepository.deleteById(plannerId);
        logger.debug("Planner 삭제 완료. Planner ID: {}", plannerId);*/
        jdbcTemplate.update("DELETE FROM planner WHERE planner_id = ?", plannerId);
        logger.debug("planner 데이터 삭제 완료. Planner ID: {}", plannerId);


        // AUTO_INCREMENT 값을 재설정
       // resetAutoIncrement();
        try {
            resetAutoIncrement();
        } catch (Exception e) {
            logger.error("resetAutoIncrement 실행 중 오류 발생: {}", e.getMessage());
            throw e;
        }



    }

    // PlannerService.java
    @Transactional
    public void completePlanner(int plannerId, String userId) {
        logger.debug("Planner 완료 처리 시작. Planner ID: {}, 사용자 ID: {}", plannerId, userId);

        // PlannerEntity 조회
        PlannerEntity planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> {
                    logger.error("Planner를 찾을 수 없습니다. Planner ID: {}", plannerId);
                    return new RuntimeException("Planner를 찾을 수 없습니다. Planner ID: " + plannerId);
                });

        // 사용자 ID가 일치하는지 확인
        if (planner.getUserId() == null) {
            logger.error("Planner의 사용자 ID가 null입니다. Planner ID: {}", plannerId);
            throw new RuntimeException("Planner의 사용자 ID가 null입니다.");
        }

        if (!planner.getUserId().getUserId().equals(userId)) {
            logger.error("사용자 ID가 Planner와 일치하지 않습니다. 사용자 ID: {}, Planner ID: {}", userId, plannerId);
            throw new RuntimeException("사용자 ID가 Planner와 일치하지 않습니다.");
        }

        // 완료 상태로 변경
        planner.setCompleted(true);
        plannerRepository.save(planner);

        logger.debug("Planner 완료 처리 성공. Planner ID: {}", plannerId);
    }

    // userId에 해당하는 플래너 목록 조회
    public List<PlannerEntity> getPlannerListByUser(String userId) {
        // UserEntity 조회
        UserEntity user = (UserEntity) userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. 사용자 ID: " + userId));

        // 해당 사용자에 대한 플래너 목록 조회
        List<PlannerEntity> planners = plannerRepository.findByUserId(user);

        // 필요 시 필터링, 변환, 정렬 등을 추가할 수 있음
        return planners;
    }




    private void resetAutoIncrement() {
        try {
            transactionTemplate.executeWithoutResult(status -> {
                // 최신 planner_id 가져오기
                String maxIdSql = "SELECT MAX(planner_id) FROM planner";
                Integer maxId = jdbcTemplate.queryForObject(maxIdSql, Integer.class);

                // AUTO_INCREMENT 값 설정
                if (maxId == null || maxId == 0) {
                    // 테이블이 비어있거나 첫 번째 항목인 경우 AUTO_INCREMENT를 1로 설정
                    String resetSql = "ALTER TABLE planner AUTO_INCREMENT = 1";
                    jdbcTemplate.update(resetSql);
                    logger.debug("AUTO_INCREMENT 값을 1로 재설정");
                } else {
                    // 최대 ID에 맞춰 AUTO_INCREMENT 값 설정
                    String resetSql = "ALTER TABLE planner AUTO_INCREMENT = ?";
                    jdbcTemplate.update(resetSql, maxId + 1);
                    logger.debug("AUTO_INCREMENT 값을 {}로 재설정", maxId + 1);
                }
            });
        } catch (Exception e) {
            logger.error("AUTO_INCREMENT 재설정 중 오류 발생", e);
            throw new RuntimeException("AUTO_INCREMENT 재설정 실패", e);
        }
    }

}