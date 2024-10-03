package com.example.modoproject.Review.controller;

import com.example.modoproject.BusinessOwnerRegister.Service.StoreService;
import com.example.modoproject.BusinessOwnerRegister.entity.Store;
import com.example.modoproject.Review.entity.Review;
import com.example.modoproject.Review.service.ReviewService;
import com.example.modoproject.login.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @PostMapping("/post")
    public ResponseEntity<String> postReview(
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("merchantUid") String merchantUid) {  // companyId 파라미터 제거
        HttpSession session = httpServletRequest.getSession();
        User user = (User) session.getAttribute("user");
        String author = user != null ? user.getNickname() : "비회원";
        String externalId = user != null ? user.getExternalId() : "anonymous";

        try {
            reviewService.saveReview(author, content, image, externalId, merchantUid);  // companyId 전달 제거
            return new ResponseEntity<>("Review posted successfully", HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to post review", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateReview(
            @PathVariable("id") Long id,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            reviewService.updateReview(id, content, image);
            return new ResponseEntity<>("Review updated successfully", HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update review", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable("id") Long id) {
        reviewService.deleteReview(id);
        return new ResponseEntity<>("Review deleted successfully", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Review>> listReviews() {
        List<Review> reviewList = reviewService.findAll();
        return new ResponseEntity<>(reviewList, HttpStatus.OK);
    }

    @GetMapping("/list/{merchantUid}") // 결제내역 - 상품에 따른 리뷰 작성
    public ResponseEntity<List<Review>> listReviewsByMerchantUid(@PathVariable("merchantUid") String merchantUid) {
        HttpSession session = httpServletRequest.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            String externalId = user.getExternalId();
            List<Review> reviews = reviewService.findByMerchantUidAndExternalId(merchantUid, externalId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable("id") Long id) {
        Optional<Review> reviewOptional = reviewService.findById(id);
        if (reviewOptional.isPresent()) {
            return new ResponseEntity<>(reviewOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/public/{merchantUid}") // 가게 상품의 모든 리뷰 조회
    public ResponseEntity<List<Review>> listAllReviewsByMerchantUid(@PathVariable("merchantUid") String merchantUid) {
        List<Review> reviews = reviewService.findByMerchantUid(merchantUid);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/listByCompany/{companyId}")
    public ResponseEntity<List<Review>> listReviewsByCompanyId(@PathVariable("companyId") String companyId) {
        List<Review> reviews = reviewService.findByCompanyId(companyId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Review>> getLatestReviews() {
        List<Review> latestReviews = reviewService.findLatestReviews();
        return new ResponseEntity<>(latestReviews, HttpStatus.OK);
    }

    @GetMapping("/myStore/reviews/{storeId}")
    public ResponseEntity<List<String>> getMyStoreReviews(@PathVariable("storeId") Long storeId) {
        Optional<Store> optionalStore = storeService.findById(storeId);

        if (optionalStore.isPresent()) {
            Store store = optionalStore.get();
            String companyId = store.getCompanyId(); // Store에서 companyId 추출

            List<Review> reviews = reviewService.findByCompanyId(companyId); // 회사 ID로 리뷰를 조회

            List<String> reviewContents = reviews.stream()
                    .map(Review::getContent)
                    .toList();

            return new ResponseEntity<>(reviewContents, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
