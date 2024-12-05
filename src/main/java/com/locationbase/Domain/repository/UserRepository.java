package com.locationbase.Domain.repository;

import com.locationbase.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    // 이메일을 기준으로 사용자 조회
    Optional<UserEntity> findByEmail(String email);

    // 사용자의 아이디로 조회 (기본 제공 메소드)
    @Override
    Optional<UserEntity> findById(String userId); // @Override 어노테이션 추가
}
