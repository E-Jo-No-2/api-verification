package com.locationbase.controller;

import com.locationbase.Domain.repository.MemoRepository; // Add this import
import com.locationbase.Domain.repository.PlannerRepository;
import com.locationbase.entity.MemoEntity;
import com.locationbase.entity.PlannerEntity;
import com.locationbase.service.MemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/memos")
public class MemoController {

    private final MemoService memoService;
    private final PlannerRepository plannerRepository;

    @Autowired
    public MemoController(MemoService memoService, PlannerRepository plannerRepository) {
        this.memoService = memoService;
        this.plannerRepository = plannerRepository;
    }

    @PostMapping
    public ResponseEntity<MemoEntity> addMemo(@RequestBody MemoEntity memoEntity) {
        if (memoEntity.getPlanner() == null || memoEntity.getPlanner().getPlannerId() == 0) {

            PlannerEntity planner = new PlannerEntity();
            planner.setDate(Optional.ofNullable(memoEntity.getPlanner().getDate()).orElse(LocalDate.now()));

            // plannerEntity 먼저 저장
            PlannerEntity savedPlanner = plannerRepository.save(memoEntity.getPlanner());
            memoEntity.setPlanner(savedPlanner);
            System.out.println("Received POST request for saving memo");
        }

        // service로 memoEntity저장
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
