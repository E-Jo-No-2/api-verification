
package com.locationbase.service;

import com.locationbase.Domain.repository.MemoRepository;
import com.locationbase.Domain.repository.PlannerRepository;
import com.locationbase.entity.MemoEntity;
import com.locationbase.entity.PlannerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final PlannerRepository plannerRepository;

    @Autowired
    public MemoService(MemoRepository memoRepository, PlannerRepository plannerRepository) {
        this.memoRepository = memoRepository;
        this.plannerRepository = plannerRepository;
    }

    public MemoEntity createMemo(String memoContent, int plannerId) {
        PlannerEntity planner = plannerRepository.findById(plannerId)
                .orElseThrow(() -> new RuntimeException("Planner not found with id: " + plannerId));

        MemoEntity memo = new MemoEntity();
        memo.setMemoContent(memoContent);
        memo.setPlanner(planner); //plannerId

        return memoRepository.save(memo);
    }

    public List<MemoEntity> getMemosByPlannerId(int plannerId) {
       return memoRepository.findByPlanner_PlannerId(plannerId);
    }

    public MemoEntity updateMemo(int memoId, String memoContent) {
        MemoEntity memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new RuntimeException("Memo not found with id: " + memoId));

        memo.setMemoContent(memoContent);
        return memoRepository.save(memo);
    }

    public void deleteMemo(int memoId) {
        if (!memoRepository.existsById(memoId)) {
            throw new RuntimeException("Memo not found with id: " + memoId);
        }
        memoRepository.deleteById(memoId);
    }
}



