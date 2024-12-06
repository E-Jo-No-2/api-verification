package com.locationbase.service;

import com.locationbase.domain.repository.MemoRepository;
import com.locationbase.entity.MemoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemoService {

    private final MemoRepository memoRepository;

    @Autowired
    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
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
