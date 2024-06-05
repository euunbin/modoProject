package com.example.modoproject.Review.service;

import com.example.modoproject.Review.entity.Review;
import com.example.modoproject.Review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    private final String uploadDir = "src/main/resources/static/review";

    public void saveReview(String title, String author, String content, MultipartFile image) throws IOException {
        Review review = new Review();
        review.setTitle(title);
        review.setAuthor(author);
        review.setContent(content);

        if (!image.isEmpty()) {
            String imageName = image.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir, imageName);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, image.getBytes());

            review.setImageUrl("/" + uploadDir + "/" + imageName);
        }

        if (review.getCreatedDateTime() == null) {
            review.setCreatedDateTime(LocalDateTime.now());
        }

        reviewRepository.save(review);
    }

    public void updateReview(Long id, String title, String content, MultipartFile image) throws IOException {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            review.setTitle(title);
            review.setContent(content);

            if (!image.isEmpty()) {
                String imageName = image.getOriginalFilename();
                Path imagePath = Paths.get(uploadDir, imageName);
                Files.createDirectories(imagePath.getParent());
                Files.write(imagePath, image.getBytes());

                review.setImageUrl("/" + uploadDir + "/" + imageName);
            }

            reviewRepository.save(review);
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
}
