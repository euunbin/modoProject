package com.example.modoproject.configuration;

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
        try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}