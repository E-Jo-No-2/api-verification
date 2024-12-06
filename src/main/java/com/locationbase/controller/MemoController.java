package com.locationbase.controller;

import com.locationbase.entity.MemoEntity;
import com.locationbase.service.MemoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/memo")
public class MemoController {

    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    // 새로운 메모 생성
    @PostMapping
    public ResponseEntity<MemoEntity> createMemo(@RequestBody MemoRequest request) {
        try {
            // MemoService를 호출하여 메모 저장
            MemoEntity savedMemo = memoService.createMemo(request.getMemoContent(), request.getPlannerId());
            return ResponseEntity.ok(savedMemo);
        } catch (Exception e) {
            // 예외 발생 시 로그 출력
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error 반환
        }
    }

    // 특정 플래너 ID로 메모 목록 조회
    @GetMapping("/planner/{plannerId}")
    public ResponseEntity<List<MemoEntity>> getMemosByPlannerId(@PathVariable int plannerId) {
        List<MemoEntity> memos = memoService.getMemosByPlannerId(plannerId);
        return ResponseEntity.ok(memos);
    }

    // 특정 메모 ID로 메모 수정
    @PutMapping("/{memoId}")
    public ResponseEntity<MemoEntity> updateMemo(@PathVariable int memoId, @RequestBody MemoRequest request) {
        MemoEntity updatedMemo = memoService.updateMemo(memoId, request.getMemoContent());
        return ResponseEntity.ok(updatedMemo);
    }

    // 특정 메모 ID로 메모 삭제
    @DeleteMapping("/{memoId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable int memoId) {
        memoService.deleteMemo(memoId);
        return ResponseEntity.noContent().build();
    }

    // 메모 요청 데이터를 위한 내부 클래스
    public static class MemoRequest {
        private String memoContent;
        private int plannerId;

        public String getMemoContent() {
            return memoContent;
        }

        public void setMemoContent(String memoContent) {
            this.memoContent = memoContent;
        }

        public int getPlannerId() {
            return plannerId;
        }

        public void setPlannerId(int plannerId) {
            this.plannerId = plannerId;
        }
    }
}
