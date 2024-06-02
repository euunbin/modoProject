package com.example.modoproject.Review.controller;

import com.example.modoproject.Review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.example.modoproject.login.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("/review/post")
    public String writeReview(Model model) {
        HttpSession session = httpServletRequest.getSession();
        User user = (User) session.getAttribute("user");
        String author = user != null ? user.getNickname() : "";
        model.addAttribute("author", author);
        return "reviewpost";
    }

    @PostMapping("/review/post")
    public String postReview(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam("image") MultipartFile image) {
        HttpSession session = httpServletRequest.getSession();
        User user = (User) session.getAttribute("user");
        String author = user != null ? user.getNickname() : "비회원";

        try {
            reviewService.saveReview(title, author, content, image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/review/post";
    }
}

