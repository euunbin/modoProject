package com.example.modoproject.controller;

import com.example.modoproject.entity.User; // 올바른 User 클래스 import
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequestMapping("/login")
public class loginController {
    private static final Logger logger = Logger.getLogger(loginController.class.getName());
    @GetMapping
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String sessionId = session.getId();
            logger.info("Session ID: " + sessionId);
        } else {
            logger.info("No active session");
        }

        return "login";
    }

    @GetMapping("/sessionlogin")
    public String sessionLogin(Model model, HttpSession session) {
        Optional<User> optionalUser = Optional.ofNullable((User) session.getAttribute("user"));
        User user = optionalUser.orElse(null);

        if (user != null) {
            model.addAttribute("user", user);
            return "sessionlogin";
        } else {
            return "login";
        }
    }
}