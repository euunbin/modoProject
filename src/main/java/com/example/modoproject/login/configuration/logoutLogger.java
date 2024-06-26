package com.example.modoproject.login.configuration;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class logoutLogger implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 세션에서 사용자 정보 제거
        HttpSession session = request.getSession();
        session.removeAttribute("userInfo");

        try {
            // 로그인 페이지로 리다이렉트
            response.sendRedirect("http://localhost:3000/Main");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}