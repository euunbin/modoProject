package com.example.modoproject.Review.service;

import com.example.modoproject.Review.entity.Review;
import com.example.modoproject.Review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    private final String uploadDir = "src/main/resources/static/reviewImg";

    public void saveReview(String author, String content, MultipartFile image, String externalId, String merchantUid) throws IOException {
        Review review = new Review();
        review.setAuthor(author);
        review.setContent(content);
        review.setExternalId(externalId);
        review.setMerchantUid(merchantUid);

        if (image != null && !image.isEmpty()) {
            String imageName = image.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir, imageName);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, image.getBytes());

            review.setImageUrl("/reviewImg/" + imageName);
        }

        review.setCreatedDateTime(LocalDateTime.now());
        reviewRepository.save(review);
    }

    @Transactional
    public void updateReview(Long id, String content, MultipartFile image) throws IOException {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            review.setContent(content);

            if (image != null && !image.isEmpty()) {
                String imageName = image.getOriginalFilename();
                Path imagePath = Paths.get(uploadDir, imageName);
                Files.createDirectories(imagePath.getParent());
                Files.write(imagePath, image.getBytes());

                review.setImageUrl("/reviewImg/" + imageName);
            }

            reviewRepository.save(review);
            System.out.println("Review updated: " + id);
        } else {
            System.out.println("Review not found: " + id);
        }
    }

    public void deleteReview(Long id) {
        reviewRepository.deleteById(id);
    }

    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public List<Review> findByMerchantUidAndExternalId(String merchantUid, String externalId) {
        return reviewRepository.findByMerchantUidAndExternalId(merchantUid, externalId);
    }
}
