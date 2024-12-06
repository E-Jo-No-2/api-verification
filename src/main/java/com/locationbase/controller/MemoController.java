
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

    // Create a new memo
    @PostMapping
    public ResponseEntity<MemoEntity> createMemo(@RequestBody MemoRequest request) {
        try {
            // MemoService를 호출하여 memo 저장
            MemoEntity savedMemo = memoService.createMemo(request.getMemoContent(), request.getPlannerId());
            return ResponseEntity.ok(savedMemo);
        } catch (Exception e) {
            // 예외 로그 출력
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // 500 Internal Server Error 반환
        }
    }

    // Get all memos by planner ID
    @GetMapping("/planner/{plannerId}")
    public ResponseEntity<List<MemoEntity>> getMemosByPlannerId(@PathVariable int plannerId) {
        List<MemoEntity> memos = memoService.getMemosByPlannerId(plannerId);
        return ResponseEntity.ok(memos);
    }

    // Update a memo by ID
    @PutMapping("/{memoId}")
    public ResponseEntity<MemoEntity> updateMemo(@PathVariable int memoId, @RequestBody MemoRequest request) {
        MemoEntity updatedMemo = memoService.updateMemo(memoId, request.getMemoContent());
        return ResponseEntity.ok(updatedMemo);
    }

    // Delete a memo by ID
    @DeleteMapping("/{memoId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable int memoId) {
        memoService.deleteMemo(memoId);
        return ResponseEntity.noContent().build();
    }

    // 내부 클래스
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


