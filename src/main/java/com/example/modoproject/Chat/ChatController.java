package com.example.modoproject.Chat;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chat-gpt")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/summarize")
    public ResponseEntity<String> summarizeReviews(@RequestBody ReviewRequest request) {
        String storeId = request.getStoreId();
        List<String> reviews = request.getReviews();

        String reviewContent = fetchLatestReviews(reviews);

        String summary = chatService.getSummaryResponse(storeId, Collections.singletonList(reviewContent));
        return ResponseEntity.ok(summary);
    }

    private String fetchLatestReviews(List<String> latestReviews) {
        StringBuilder reviewContent = new StringBuilder();
        for (String review : latestReviews) {
            reviewContent.append(review).append(" "); // 리뷰 내용을 이어붙임
        }
        return reviewContent.toString().trim();
    }
}

@Data // Lombok의 @Data 어노테이션으로 getter, setter 생성
class ReviewRequest {
    private String storeId;
    private List<String> reviews;
}
