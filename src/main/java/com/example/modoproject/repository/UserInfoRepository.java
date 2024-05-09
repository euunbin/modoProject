package com.example.modoproject.repository;

import com.example.modoproject.entity.User;
import com.example.modoproject.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

}

