package com.example.modoproject.login.repository;

import com.example.modoproject.login.entity.User;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface userRepository extends JpaRepository<User, Long> {
    User findByExternalId(String externalId);
}
