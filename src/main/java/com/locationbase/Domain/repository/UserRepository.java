package com.locationbase.domain.repository;

import com.locationbase.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {


    // 사용자의 아이디로 조회 (기본 제공 메소드)
    Optional<UserEntity> findById(String userId); // 변수명을 userId로 변경
}
