package com.example.modoproject.controller;

import com.example.modoproject.entity.UserInfo;
import com.example.modoproject.repository.UserInfoRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

import static com.mysql.cj.conf.PropertyKey.logger;

@Controller
public class UserInfoController {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private HttpSession httpSession;


    @GetMapping("/userinfo")
    public String showForm(Model model) {
        UserInfo userInfo = (UserInfo) httpSession.getAttribute("userInfo");
        if (userInfo != null) {
            model.addAttribute("userInfo", userInfo);
        } else {
            model.addAttribute("userInfo", new UserInfo());
        }
        return "userform";
    }

    @PostMapping("/userinfo")
    public String submitForm(UserInfo userInfo) {
        String externalId = (String) httpSession.getAttribute("externalId");

        if (externalId == null || externalId.isEmpty()) {
            return "redirect:/userinfo"; // 로그인하지 않고 주소 저장 시 임의 페이지로 리턴
        }

        // 기존 UserInfo 검색
        List<UserInfo> existingUserInfos = userInfoRepository.findByExternalId(externalId);

        if (!existingUserInfos.isEmpty()) {
            UserInfo existingUserInfo = existingUserInfos.get(0); // 첫 번째 결과 사용
            existingUserInfo.setPhoneNumber(userInfo.getPhoneNumber());
            existingUserInfo.setEmail(userInfo.getEmail());
            existingUserInfo.setPostcode(userInfo.getPostcode());
            existingUserInfo.setAddress(userInfo.getAddress());
            existingUserInfo.setDetailAddress(userInfo.getDetailAddress());
            existingUserInfo.setExtraAddress(userInfo.getExtraAddress());
            existingUserInfo.constructFullAddress();
            userInfoRepository.save(existingUserInfo);
            httpSession.setAttribute("userInfo", existingUserInfo);
        } else {
            userInfo.setExternalId(externalId);
            userInfo.constructFullAddress();
            userInfoRepository.save(userInfo);
            httpSession.setAttribute("userInfo", userInfo);
        }

        return "redirect:/userinfo/success";
    }

    @GetMapping("/userinfo/success")
    public String showSuccessPage() {
        return "userinforeturn";
    }

    @GetMapping("/userinfo/get")
    public String getUserInfo(Model model) {
        UserInfo userInfo = (UserInfo) httpSession.getAttribute("userInfo");
        if (userInfo != null) {
            model.addAttribute("userInfo", userInfo);
            return "user_info";
        } else {
            return "userinforeturn"; //userInfo가 null값인 경우, 임의 페이지로 리턴되도록 설정해둠
        }
    }

}
