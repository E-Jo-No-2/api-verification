package com.locationbase.controller;

import com.locationbase.entity.MemoEntity;
import com.locationbase.service.MemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/memos")
public class MemoController {

    private static final Logger logger = LoggerFactory.getLogger(MemoController.class);

    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    // 특정 플래너 ID로 메모 목록 조회
    @GetMapping("/planner/{plannerId}")
    public ResponseEntity<List<MemoEntity>> getMemosByPlannerId(@PathVariable("plannerId") int plannerId) {
        logger.info("메모 조회 요청: 플래너 ID={}", plannerId);
        try {
            List<MemoEntity> memos = memoService.getMemosByPlannerId(plannerId);
            logger.info("메모 조회 성공: 조회된 메모 수={}, 메모 목록={}", memos.size(), memos);
            return ResponseEntity.ok(memos);
        } catch (Exception e) {
            logger.error("메모 조회 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).build();
        }
    }

    // 특정 메모 ID로 메모 수정
    @PutMapping("/{memoId}")
    public ResponseEntity<MemoEntity> updateMemo(  @PathVariable("memoId") int memoId, @RequestBody MemoEntity memoEntity) {
        logger.info("메모 수정 요청: 메모 ID={}, 새로운 메모 내용={}", memoId, memoEntity.getMemoContent());
        try {
            MemoEntity updatedMemo = memoService.updateMemo(memoId, memoEntity.getMemoContent());
            logger.info("메모 수정 성공: {}", updatedMemo);
            return ResponseEntity.ok(updatedMemo);
        } catch (Exception e) {
            logger.error("메모 수정 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

}
