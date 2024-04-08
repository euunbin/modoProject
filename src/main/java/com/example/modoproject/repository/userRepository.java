package com.example.modoproject.repository;

import com.example.modoproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepository extends JpaRepository<User, Long> {
    // 추가적인 메서드가 필요하다면 여기에 선언합니다.
}
