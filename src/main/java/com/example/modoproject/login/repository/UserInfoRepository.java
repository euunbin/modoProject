package com.example.modoproject.login.repository;

import com.example.modoproject.login.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    List<UserInfo> findByExternalId(String externalId);
    boolean existsByAlias(String alias);
}

