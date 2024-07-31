package com.example.modoproject.login.controller;

import com.example.modoproject.login.entity.UserInfo;
import com.example.modoproject.login.repository.UserInfoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userinfo")
public class UserInfoController {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private HttpSession httpSession;

    @GetMapping
    public ResponseEntity<UserInfo> getUserInfo() {
        UserInfo userInfo = (UserInfo) httpSession.getAttribute("userInfo");
        if (userInfo == null) {
            return ResponseEntity.ok(new UserInfo());
        }
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping
    public ResponseEntity<Object> submitForm(@RequestBody UserInfo userInfo) {
        String externalId = (String) httpSession.getAttribute("externalId");

        if (externalId == null || externalId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 시도해 주세요.");
        }

        List<UserInfo> existingUserInfos = userInfoRepository.findByExternalId(externalId);

        if (!existingUserInfos.isEmpty()) {
            UserInfo existingUserInfo = existingUserInfos.get(0);
            existingUserInfo.setPhoneNumber(userInfo.getPhoneNumber());
            existingUserInfo.setEmail(userInfo.getEmail());
            existingUserInfo.setPostcode(userInfo.getPostcode());
            existingUserInfo.setAddress(userInfo.getAddress());
            existingUserInfo.setDetailAddress(userInfo.getDetailAddress());
            existingUserInfo.setExtraAddress(userInfo.getExtraAddress());
            existingUserInfo.constructFullAddress();
            userInfoRepository.save(existingUserInfo);
            httpSession.setAttribute("userInfo", existingUserInfo);
            return ResponseEntity.ok(existingUserInfo);
        } else {
            userInfo.setExternalId(externalId);
            userInfo.constructFullAddress();
            userInfoRepository.save(userInfo);
            httpSession.setAttribute("userInfo", userInfo);
            return ResponseEntity.ok(userInfo);
        }
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "userinforeturn";
    }

    @GetMapping("/external")
    public ResponseEntity<UserInfo> getExternalUserInfo() {
        String userExternalId = (String) httpSession.getAttribute("externalId");
        List<UserInfo> userInfos = userInfoRepository.findByExternalId(userExternalId);

        if (!userInfos.isEmpty()) {
            UserInfo userInfo = userInfos.get(0);
            userInfo.constructFullAddress();
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.ok(null);
        }
    }
}
