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

            review.setImageUrl("/" + uploadDir + imageName);
        }

        reviewRepository.save(review);
    }
}
