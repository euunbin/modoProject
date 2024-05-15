package com.example.modoproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class loginController {
    @GetMapping
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 세션에서 사용자 정보 제거
        HttpSession session = request.getSession();
        session.removeAttribute("userInfo");

        // 세션 무효화
        session.invalidate();

        return "login";
    }
}