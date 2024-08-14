package com.example.modoproject.Review.controller;

import com.example.modoproject.Review.entity.Review;
import com.example.modoproject.Review.entity.ReviewComment;
import com.example.modoproject.Review.repository.ReviewCommentRepository;
import com.example.modoproject.Review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/review")
public class ReviewCommentController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewCommentRepository reviewCommentRepository;

    @GetMapping("/{reviewId}/comments")
    public ResponseEntity<?> getComments(@PathVariable("reviewId") Long reviewId) {
        Optional<Review> optionalReview = reviewService.findById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            List<ReviewComment> comments = reviewCommentRepository.findByReviewId(reviewId);
            return ResponseEntity.ok(comments);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{reviewId}/comments")
    public ResponseEntity<?> addComment(@PathVariable("reviewId") Long reviewId,
                                        @RequestBody ReviewComment commentRequest) {
        Optional<Review> optionalReview = reviewService.findById(reviewId);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();

            ReviewComment comment = new ReviewComment();
            comment.setContent(commentRequest.getContent());
            comment.setReview(review);
            reviewCommentRepository.save(comment);

            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId,
                                           @RequestBody ReviewComment commentRequest) {
        Optional<ReviewComment> optionalComment = reviewCommentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            ReviewComment comment = optionalComment.get();
            comment.setContent(commentRequest.getContent());
            reviewCommentRepository.save(comment);
            return ResponseEntity.ok(comment);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId) {
        Optional<ReviewComment> optionalComment = reviewCommentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            ReviewComment comment = optionalComment.get();
            Long reviewId = comment.getReview().getId();
            reviewCommentRepository.delete(comment);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
