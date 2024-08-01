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
import java.util.List;
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
    public String postReview(
                             @RequestParam("content") String content,
                             @RequestParam("image") MultipartFile image) {
        HttpSession session = httpServletRequest.getSession();
        User user = (User) session.getAttribute("user");
        String author = user != null ? user.getNickname() : "비회원";
        String externalId = user != null ? user.getExternalId() : "anonymous";


        try {
            reviewService.saveReview(author, content, image, externalId);
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
        return "redirect:/review/list";
    }

    @PostMapping("/review/edit")
    public String updateReview(@RequestParam("id") Long id,
                               @RequestParam("content") String content,
                               @RequestParam("image") MultipartFile image) {
        try {
            reviewService.updateReview(id, content, image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/review/list";
    }

    @DeleteMapping("/review/delete/{id}") //reviewdatail 페이지 없음 그래서 아직 수정삭제 안됨
    public String deleteReview(@PathVariable("id") Long id) {
        reviewService.deleteReview(id);
        return "redirect:/review/list";
    }
    @GetMapping("/review/list")
    public String listReviews(Model model) {
        List<Review> reviewList = reviewService.findAll();
        model.addAttribute("reviewList", reviewList);
        return "reviewlist";
    }

    @GetMapping("/review/list/{id}")
    public String reviewDetail(@PathVariable("id") Long id, Model model) {
        Optional<Review> optionalReview = reviewService.findById(id);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            model.addAttribute("review", review);
            return "reviewdetail";
        }
        return "redirect:/review/list";
    }
}

