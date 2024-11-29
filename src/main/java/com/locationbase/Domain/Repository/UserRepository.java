package com.locationbase.Domain.Repository;

import com.locationbase.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    // 이메일을 기준으로 사용자 조회
    Optional<UserEntity> findByEmail(String email);

    // 사용자의 아이디로 조회 (기본 제공 메소드)
    Optional<UserEntity> findById(String user_id);
}
