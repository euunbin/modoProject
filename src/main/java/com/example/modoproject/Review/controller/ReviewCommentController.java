package com.example.modoproject.Review.controller;

import com.example.modoproject.Review.entity.Review;
import com.example.modoproject.Review.entity.ReviewComment;
import com.example.modoproject.Review.repository.ReviewCommentRepository;
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
public class ReviewCommentController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewCommentRepository reviewCommentRepository;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping("/review/comment/{reviewId}")
    public String showReviewDetail(@PathVariable("reviewId") Long reviewId, Model model) {
        Optional<Review> optionalReview = reviewService.findById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            model.addAttribute("review", review);

            List<ReviewComment> comments = reviewCommentRepository.findByReviewId(reviewId);
            model.addAttribute("comments", comments);

            return "reviewdetail";
        }
        return "redirect:/review/list";
    }

    @PostMapping("/review/comment/{reviewId}")
    public String addComment(@PathVariable("reviewId") Long reviewId,
                             @RequestParam("commentContent") String content) {
        Optional<Review> optionalReview = reviewService.findById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();

            ReviewComment comment = new ReviewComment();
            comment.setContent(content);
            comment.setReview(review);

            reviewCommentRepository.save(comment);
        }
        return "redirect:/review/comment/" + reviewId; // 리뷰 상세 페이지로 리디렉션
    }
    @GetMapping("/review/comment/edit/{commentId}")
    public String showEditCommentForm(@PathVariable("commentId") Long commentId, Model model) {
        Optional<ReviewComment> optionalComment = reviewCommentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            ReviewComment comment = optionalComment.get();
            model.addAttribute("comment", comment);
            return "editcomment";
        }
        return "redirect:/review/list";
    }

    @PostMapping("/review/comment/edit/{commentId}")
    public String editComment(@PathVariable("commentId") Long commentId,
                              @RequestParam("commentContent") String content) {
        Optional<ReviewComment> optionalComment = reviewCommentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            ReviewComment comment = optionalComment.get();
            comment.setContent(content);
            reviewCommentRepository.save(comment);
            return "redirect:/review/comment/" + comment.getReview().getId(); // 리뷰 상세 페이지로 리디렉션
        }
        return "redirect:/review/list";
    }

    @PostMapping("/review/comment/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId) {
        Optional<ReviewComment> optionalComment = reviewCommentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            ReviewComment comment = optionalComment.get();
            Long reviewId = comment.getReview().getId();
            reviewCommentRepository.delete(comment);
            return "redirect:/review/comment/" + reviewId; // 리뷰 상세 페이지로 리디렉션
        }
        return "redirect:/review/list";
    }
}
