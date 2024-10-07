package com.example.modoproject.Chat;

import com.example.modoproject.Review.entity.Review;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@EnableScheduling
@RequiredArgsConstructor
public class ChatService {

    private final ChatgptService chatgptService;
    private final Map<String, String> cachedSummary = new HashMap<>();
    private final Map<String, Instant> summaryTimestamps = new HashMap<>();
    private final Map<String, List<String>> latestReviewsMap = new HashMap<>();

    public String getChatResponse(String prompt) {
        String formattedPrompt = prompt + " 다음 형식으로 대답해 주세요: {\"answer\": }";
        String response = chatgptService.sendMessage(formattedPrompt);
        return formatResponse(response);
    }

    public String getSummaryResponse(String storeId, List<String> reviews) {
        // 캐시된 요약이 존재하고, 1분 이내에 요청된 경우 기존 요약을 반환
        if (cachedSummary.containsKey(storeId) &&
                Instant.now().minusSeconds(60).isBefore(summaryTimestamps.get(storeId))) {
            return cachedSummary.get(storeId);
        }

        String reviewContent = String.join(" ", reviews);

        String prompt = String.format("""
        받아온 리뷰에서 반드시 **다음 선택지 중 최대 세 가지**를 골라서 응답하세요. 반드시 선택지 외의 답변은 절대 하지 마세요. 

        선택지:
        1. 맛있어요
        2. 배달이 빨라요
        3. 양이 많아요
        4. 포장이 깔끔해요
        5. 고객 서비스가 좋아요
        6. 가격이 합리적이에요

        응답 규칙:
        - 반드시 위의 선택지 중에서만 답변하세요.
        - 반드시 **세 가지**를 선택하고, 선택지 외의 답변이나 설명은 절대 하지 마세요.
        - 응답은 반드시 **콤마(,)**로 구분하세요.
        - 반드시 **JSON 형식**으로 답변을 주세요.
        - JSON 형식은 반드시 **{answer: '응답 내용'}**이어야 하고, 내용의 따옴표는 **싱글 쿼테이션(')**을 사용하세요.

        예시 응답: {answer: '맛있어요, 배달이 빨라요, 고객 서비스가 좋아요'}

        요약할 리뷰들:
        %s
        """, reviewContent);

        String response = getChatResponse(prompt);
        cachedSummary.put(storeId, response);
        summaryTimestamps.put(storeId, Instant.now());
        return response;
    }

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void regenerateSummary() {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("요약 갱신 작업 시작 (" + currentTime + ")");
        for (String storeId : latestReviewsMap.keySet()) {
            List<String> latestReviews = latestReviewsMap.get(storeId);
            if (latestReviews != null && !latestReviews.isEmpty()) {
                String newSummary = getSummaryResponse(storeId, latestReviews);
            } else {
                System.out.println("리뷰가 없거나 비어있습니다. 기존 요약 유지.");
            }
        }
    }

    public void updateReviews(String storeId, List<String> reviews) {
        latestReviewsMap.put(storeId, reviews);
    }

    public String getCachedSummary(String storeId) {
        return cachedSummary.getOrDefault(storeId, "아직 요약된 리뷰가 없습니다.");
    }

    private String formatResponse(String response) {
        if (response.contains("answer")) {
            return response.replace("'", "\"");
        }
        return "{\"answer\": \"" + response.trim() + "\"}";
    }
}