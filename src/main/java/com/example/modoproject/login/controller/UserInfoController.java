package com.example.modoproject.login.controller;

import com.example.modoproject.login.entity.UserInfo;
import com.example.modoproject.login.repository.UserInfoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (existingUserInfos.size() >= 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("최대 5개의 정보만 저장할 수 있습니다.");
        }

        userInfo.setExternalId(externalId);
        userInfo.constructFullAddress();

        userInfoRepository.save(userInfo);

        httpSession.setAttribute("userInfo", userInfo);

        return ResponseEntity.ok(userInfo);
    }



    @GetMapping("/success")
    public String showSuccessPage() {
        return "userinforeturn";
    }

    @GetMapping("/external")
    public ResponseEntity<List<UserInfo>> getExternalUserInfo() {
        String userExternalId = (String) httpSession.getAttribute("externalId");
        List<UserInfo> userInfos = userInfoRepository.findByExternalId(userExternalId);

        if (!userInfos.isEmpty()) {
            for (UserInfo userInfo : userInfos) {
                userInfo.constructFullAddress();
            }
            return ResponseEntity.ok(userInfos);
        } else {
            return ResponseEntity.ok(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserInfo(@PathVariable Long id) {
        String externalId = (String) httpSession.getAttribute("externalId");

        if (externalId == null || externalId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 시도해 주세요.");
        }

        UserInfo userInfo = userInfoRepository.findById(id).orElse(null);

        if (userInfo == null || !userInfo.getExternalId().equals(externalId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 정보를 찾을 수 없습니다.");
        }

        userInfoRepository.delete(userInfo);

        return ResponseEntity.ok("정보가 성공적으로 삭제되었습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUserInfo(@PathVariable Long id, @RequestBody UserInfo updatedUserInfo) {
        String externalId = (String) httpSession.getAttribute("externalId");

        if (externalId == null || externalId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 시도해 주세요.");
        }

        UserInfo existingUserInfo = userInfoRepository.findById(id).orElse(null);

        if (existingUserInfo == null || !existingUserInfo.getExternalId().equals(externalId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 정보를 찾을 수 없습니다.");
        }

        existingUserInfo.setPhoneNumber(updatedUserInfo.getPhoneNumber());
        existingUserInfo.setEmail(updatedUserInfo.getEmail());
        existingUserInfo.setPostcode(updatedUserInfo.getPostcode());
        existingUserInfo.setAddress(updatedUserInfo.getAddress());
        existingUserInfo.setDetailAddress(updatedUserInfo.getDetailAddress());
        existingUserInfo.setExtraAddress(updatedUserInfo.getExtraAddress());
        existingUserInfo.setAlias(updatedUserInfo.getAlias());

        existingUserInfo.constructFullAddress();

        userInfoRepository.save(existingUserInfo);

        return ResponseEntity.ok(existingUserInfo);
    }

    @GetMapping("/check-alias")
    public Map<String, Boolean> checkAlias(@RequestParam("alias") String alias) {
        boolean isDuplicate = userInfoRepository.existsByAlias(alias);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicate);
        return response;
    }
}
