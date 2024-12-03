package com.locationbase.service;

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

        return memoRepository.save(memoEntity);
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
}




