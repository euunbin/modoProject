package com.example.modoproject.Review.repository;

import com.example.modoproject.Review.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
    List<ReviewComment> findByReviewId(Long reviewId);
}
