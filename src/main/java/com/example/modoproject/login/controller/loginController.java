package com.example.modoproject.login.controller;

import com.example.modoproject.BusinessOwnerRegister.Service.StoreService;
import com.example.modoproject.login.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.logging.Logger;

@Controller
@RequestMapping("/login")
public class loginController {
    private static final Logger logger = Logger.getLogger(loginController.class.getName());

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private StoreService storeService;

    @GetMapping
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String externalId = (String) session.getAttribute("externalId");

            if (externalId != null) {
                // StoreService를 사용하여 companyId 가져오기
                String companyId = storeService.getCompanyIdByExternalId(externalId);

                if (companyId != null) {
                    logger.info("External ID: " + externalId);
                    logger.info("Company ID: " + companyId);

                    session.setAttribute("externalId", externalId);
                    session.setAttribute("companyId", companyId);

                    User user = (User) session.getAttribute("user");
                    if (user != null) {
                        String role = user.getRole();
                        session.setAttribute("role", role); // 세션에 role 저장
                    }
                } else {
                    logger.info("External ID: " + externalId);
                    logger.info("Company ID: 업체 미등록");

                    session.setAttribute("externalId", externalId);
                    session.setAttribute("companyId", "업체 미등록"); // 회사 ID가 없는 경우에 대한 처리
                }
            }
        } else {
            logger.info("No active session.");
        }

        return "redirect:http://localhost:3000/Main";
    }

    @GetMapping("/logout")
    @ResponseBody
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("userInfo");
        session.removeAttribute("externalId");
        session.removeAttribute("companyId");
        session.removeAttribute("role");
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

    @GetMapping("/user/role")
    @ResponseBody
    public String getUserRole() {
        User user = (User) httpSession.getAttribute("user");
        if (user != null) {
            return "{\"role\":\"" + user.getRole() + "\"}";
        } else {
            return "{\"role\":\"\"}";
        }
    }
    @GetMapping("/user/externalId")
    @ResponseBody
    public ResponseEntity<String> getExternalId(HttpSession session) {
        String externalId = (String) session.getAttribute("externalId");
        if (externalId != null) {
            return ResponseEntity.ok(externalId);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("External ID not found");
        }
    }

}