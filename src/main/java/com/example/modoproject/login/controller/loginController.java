package com.example.modoproject.login.controller;

import com.example.modoproject.login.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@Controller
@RequestMapping("/login")
public class loginController {
    private static final Logger logger = Logger.getLogger(loginController.class.getName());

    @Autowired
    private HttpSession httpSession;

    @GetMapping
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String sessionId = session.getId();
            logger.info("Session ID: " + sessionId);
        } else {
            logger.info("No active session");
        }

        return "redirect:http://localhost:3000/Main";
    }
    @GetMapping("/logout")
    @ResponseBody
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("userInfo");
        session.invalidate();
        return "{\"message\":\"로그아웃 되었습니다.\", \"redirectUrl\":\"http://localhost:3000/Main\"}";
    }

    @GetMapping("/sessionlogin")
    public String sessionLogin(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            model.addAttribute("user", user);
            return "sessionlogin";
        } else {
            return "login";
        }
    }

    @GetMapping("/user/nickname")
    @ResponseBody
    public String getUserNickname() {
        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            return "{\"nickname\":\"" + user.getNickname() + "\"}";
        } else {
            return "{\"nickname\":\"\"}";
        }
    }
}
