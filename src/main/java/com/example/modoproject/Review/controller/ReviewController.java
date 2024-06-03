package com.example.modoproject.Review.controller;

import com.example.modoproject.Review.entity.Review;
import com.example.modoproject.Review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import com.example.modoproject.login.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

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
    @GetMapping("/review/edit/{id}")
    public String editReview(@PathVariable("id") Long id, Model model) {
        Optional<Review> optionalReview = reviewService.findById(id);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            model.addAttribute("review", review);
            return "editreview";
        }
        return "reviewpost";
    }

    @PostMapping("/review/edit")
    public String updateReview(@RequestParam("id") Long id,
                               @RequestParam("title") String title,
                               @RequestParam("content") String content,
                               @RequestParam("image") MultipartFile image) {
        try {
            reviewService.updateReview(id, title, content, image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "reviewpost";
    }

    @DeleteMapping("/review/delete/{id}") //reviewdatail 페이지 없음 그래서 아직 수정삭제 안됨
    public String deleteReview(@PathVariable("id") Long id) {
        reviewService.deleteReview(id);
        return "redirect:/review/post";
    }

}

