package com.example.modoproject.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;
    private String profile_image;
    private String clientId;
    private String providerName;
    private String role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserInfo userInfo;

    // 생성자, 게터 및 세터
    public User() {}

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProfileImage(String profileImage) {
        this.profile_image = profileImage;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public void setRole(String role) {this.role = role;}

    public Long getId() {
        return id;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        userInfo.setUser(this);
    }
}
