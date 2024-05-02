package com.example.modoproject.controller;

import com.example.modoproject.entity.UserInfo;
import com.example.modoproject.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserInfoController {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @GetMapping("/userinfo")
    public String showForm(Model model) {
        model.addAttribute("userInfo", new UserInfo());
        return "userform";
    }

    @PostMapping("/userinfo")
    public String submitForm(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
        return "redirect:/userinfo/success";
    }

    @GetMapping("/userinfo/success")
    public String showSuccessPage() {
        return "userinforeturn";
    }
}
