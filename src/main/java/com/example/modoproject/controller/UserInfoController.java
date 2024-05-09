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
        userInfoRepository.save(userInfo);
        httpSession.setAttribute("userInfo", userInfo);
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
