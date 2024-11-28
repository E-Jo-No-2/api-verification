package com.locationbase.Controller;

import com.locationbase.Entity.MemoEntity;
import com.locationbase.Service.MemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/memos")
public class MemoController {

    private final MemoService memoService;

    @Autowired
    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @PostMapping
    public ResponseEntity<MemoEntity> addMemo(@RequestBody MemoEntity memoEntity) {
        MemoEntity savedMemo = memoService.addMemo(memoEntity);
        return ResponseEntity.ok(savedMemo);
    }

    @PutMapping("/{memoId}")
    public ResponseEntity<MemoEntity> updateMemo(@PathVariable int memoId, @RequestBody String memoContent) {
        try {
            MemoEntity updatedMemo = memoService.updateMemo(memoId, memoContent);
            return ResponseEntity.ok(updatedMemo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }


    @DeleteMapping("/{memoId}")
    public ResponseEntity<Void> deleteMemo(@PathVariable int memoId) {
        try {
            memoService.deleteMemo(memoId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build();
        }
    }
}