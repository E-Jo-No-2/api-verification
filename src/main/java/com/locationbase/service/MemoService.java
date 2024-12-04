/*package com.locationbase.service;

import com.locationbase.Domain.repository.MemoRepository;
import com.locationbase.Domain.repository.PlannerRepository;
import com.locationbase.entity.MemoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final PlannerRepository plannerRepository;

    @Autowired
    public MemoService(MemoRepository memoRepository, PlannerRepository plannerRepository) {
        this.memoRepository = memoRepository;
        this.plannerRepository = plannerRepository;

    }

    // 새로운 메모 추가
    @Transactional
    public MemoEntity addMemo(MemoEntity memoEntity) {

        try {
            // Additional checks or logs can go here
            return memoRepository.save(memoEntity);
        } catch (Exception e) {
            // Log the error for more details
            System.err.println("Error saving MemoEntity: " + e.getMessage());
            throw new RuntimeException("Error saving memo", e);
        }
    }

    // 메모 수정
    @Transactional
    public MemoEntity updateMemo(int memoId, String memoContent) {
        Optional<MemoEntity> optionalMemo = memoRepository.findById(memoId);
        if (optionalMemo.isPresent()) {
            MemoEntity memoEntity = optionalMemo.get();
            memoEntity.setMemoContent(memoContent);
            return memoRepository.save(memoEntity);
        }
        throw new RuntimeException("Memo not found with id: " + memoId);
    }

    // 메모 삭제
    @Transactional
    public void deleteMemo(int memoId) {

        memoRepository.deleteById(memoId);
    }
}*/

/*package com.locationbase.service;

import com.locationbase.entity.MemoEntity;
import com.locationbase.entity.PlannerEntity;
import com.locationbase.Domain.repository.MemoRepository;
import com.locationbase.Domain.repository.PlannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final PlannerRepository plannerRepository;

    @Autowired
    public MemoService(MemoRepository memoRepository, PlannerRepository plannerRepository) {
        this.memoRepository = memoRepository;
        this.plannerRepository = plannerRepository;
    }

    // Method to save memo
    public MemoEntity saveMemo(MemoEntity memoEntity) {
        // Find the planner based on the planner_id (you could validate if planner_id exists)
        Optional<PlannerEntity> plannerOptional = plannerRepository.findById(memoEntity.getPlanner().getPlannerId());
        if (plannerOptional.isPresent()) {
            PlannerEntity planner = plannerOptional.get();
            // Set the planner to the memo
            memoEntity.setPlanner(planner);
            memoEntity.setWriteDate(LocalDate.now());  // Set current date as write_date
            return memoRepository.save(memoEntity);  // Save the memo entity
        } else {
            throw new RuntimeException("Planner not found for ID: " + memoEntity.getPlanner().getPlannerId());
        }
    }
}*/
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
        memo.setPlanner(planner);

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



