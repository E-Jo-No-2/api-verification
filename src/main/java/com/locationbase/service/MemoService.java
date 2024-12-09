package com.locationbase.service;

import com.locationbase.Domain.repository.MemoRepository;
import com.locationbase.Domain.repository.PlannerRepository;
import com.locationbase.entity.MemoEntity;
import com.locationbase.entity.PlannerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoService {

    private static final Logger logger = LoggerFactory.getLogger(MemoService.class);

    private final MemoRepository memoRepository;
    private final PlannerRepository plannerRepository;

    public MemoService(MemoRepository memoRepository, PlannerRepository plannerRepository) {
        this.memoRepository = memoRepository;
        this.plannerRepository = plannerRepository;
    }


    // 특정 플래너 ID로 메모 목록 조회
    public List<MemoEntity> getMemosByPlannerId(int planner_id) {
        logger.info("특정 플래너의 메모 목록 조회: 플래너 ID={}", planner_id);
        return memoRepository.findByPlanner_PlannerId(planner_id);
    }

    // 메모 수정
    public MemoEntity updateMemo(int memoId, String memoContent) {
        logger.info("메모 수정 요청: 메모 ID={}, 새로운 내용={}", memoId, memoContent);

        MemoEntity memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new RuntimeException("메모를 찾을 수 없습니다. 메모 ID=" + memoId));

        memo.setMemoContent(memoContent);
        MemoEntity updatedMemo = memoRepository.save(memo);
        logger.info("메모 수정 성공: {}", updatedMemo);
        return updatedMemo;
    }

    // 메모 삭제
    public void deleteMemo(int memoId) {
        logger.info("메모 삭제 요청: 메모 ID={}", memoId);

        if (!memoRepository.existsById(memoId)) {
            throw new RuntimeException("메모를 찾을 수 없습니다. 메모 ID=" + memoId);
        }

        memoRepository.deleteById(memoId);
        logger.info("메모 삭제 성공: 메모 ID={}", memoId);
    }
}
