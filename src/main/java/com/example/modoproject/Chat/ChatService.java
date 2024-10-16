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
import java.util.*;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ChatService {

    private final ChatgptService chatgptService;
    private final Map<String, String> cachedSummary = new HashMap<>();
    private final Map<String, Instant> summaryTimestamps = new HashMap<>();
    private final Map<String, List<String>> latestReviewsMap = new HashMap<>();

    // 선택지와 이모티콘 배열 정의
    private static final List<String> VALID_OPTIONS = List.of(
            "맛있어요",
            "배달이 빨라요",
            "양이 많아요",
            "포장이 깔끔해요",
            "고객 서비스가 좋아요",
            "가성비",
            "신선해요",
            "메뉴가 다양해요"
    );

    private static final String[] EMOJIS = {
            "😋", // 1. 맛있어요
            "🛵", // 2. 배달이 빨라요
            "🍴", // 3. 양이 많아요
            "🍱", // 4. 포장이 깔끔해요
            "🤝", // 5. 고객 서비스가 좋아요
            "💸", // 6. 가성비
            "🌿", // 7. 신선해요
            "🎆"  // 8. 메뉴가 다양해요
    };

    public String getChatResponse(String prompt) {
        String formattedPrompt = prompt + " 다음 형식으로 대답해 주세요: {\"answer\": }";
        String response = chatgptService.sendMessage(formattedPrompt);
        return formatResponse(response);
    }

    public String getSummaryResponse(String storeId, List<String> reviews) {
        // 캐시된 요약이 존재하고, 24시간 이내에 요청된 경우 기존 요약을 반환
        if (cachedSummary.containsKey(storeId) &&
                summaryTimestamps.containsKey(storeId) &&
                Instant.now().minusSeconds(86400).isBefore(summaryTimestamps.get(storeId))) {
            return cachedSummary.get(storeId);
        }

        String reviewContent = String.join(" ", reviews);

        String prompt = String.format(""" 
                리뷰를 요약해 주세요. 아래와 같은 형식으로 세 가지 선택지 중에서 답변해 주세요:\s
                { "answer": "맛있어요, 가성비, 신선해요" }
                선택지:
                맛있어요
                배달이 빨라요
                양이 많아요
                포장이 깔끔해요
                고객 서비스가 좋아요
                가성비
                신선해요
                메뉴가 다양해요

                응답 규칙:
                - 반드시 위의 선택지 중에서만 답변하세요.
                - 반드시 **세 가지**를 선택하고, 선택지 외의 답변이나 설명은 절대 하지 마세요.
                - 응답은 반드시 **콤마(,)**로 구분하세요.
                - 반드시 **JSON 형식**으로 답변을 주세요.
                - JSON 형식은 반드시 **{answer: '응답 내용'}**이어야 하고, 내용의 따옴표는 **싱글 쿼테이션(')**을 사용하세요.

                요약할 리뷰들:
                %s
                """, reviewContent);

        String response = getChatResponse(prompt);

        if (!response.contains("answer")) {
            System.out.println("예상하지 못한 응답 형식: " + response);
        }
        cachedSummary.put(storeId, response);
        summaryTimestamps.put(storeId, Instant.now());
        return response;
    }

    @Scheduled(fixedRate = 86400000) // 24시간마다 실행
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
        // "answer"가 포함된 경우
        if (response.contains("answer")) {
            String answerContent = extractAnswerContent(response);
            if (answerContent != null) {
                String filteredContent = filterValidOptions(answerContent);
                return String.format("{\"answer\": \"%s\"}", filteredContent);
            }
        }

        // "answer"가 없지만 선택지에 있는 내용이 포함된 경우
        String validContent = extractValidOptions(response);
        if (validContent != null) {
            return String.format("{\"answer\": \"%s\"}", validContent);
        }

        // 잘못된 경우
        return null;
    }

    private String extractValidOptions(String response) {
        List<String> foundOptions = new ArrayList<>();

        for (String option : VALID_OPTIONS) {
            if (response.contains(option)) {
                int index = VALID_OPTIONS.indexOf(option);
                if (index < EMOJIS.length) {
                    foundOptions.add(EMOJIS[index] + " " + option);
                }
            }
        }

        return foundOptions.size() > 0 ? String.join(", ", foundOptions.subList(0, Math.min(foundOptions.size(), 3))) : null;
    }

    private String extractAnswerContent(String response) {
        int startIndex = response.indexOf("\"answer\":") + "\"answer\": \"".length();
        int endIndex = response.indexOf("\"", startIndex);

        if (startIndex == -1 || endIndex == -1 || startIndex >= endIndex) {
            return null;
        }

        // 이중 따옴표와 단일 따옴표 모두 처리
        String extracted = response.substring(startIndex, endIndex).trim();
        extracted = extracted.replace("\"\"", "\"");

        return extracted;
    }

    private String filterValidOptions(String answerContent) {
        String[] answers = answerContent.split(",\\s*");
        List<String> filteredAnswers = new ArrayList<>();
        Set<String> seenAnswers = new HashSet<>();

        for (String answer : answers) {
            answer = answer.trim();
            int index = VALID_OPTIONS.indexOf(answer);

            if (index != -1 && index < EMOJIS.length) {
                if (!seenAnswers.contains(answer)) {
                    filteredAnswers.add(EMOJIS[index] + " " + answer);
                    seenAnswers.add(answer);
                }
            }

            if (filteredAnswers.size() == 3) {
                break;
            }
        }

        return filteredAnswers.isEmpty() ? null : String.join(", ", filteredAnswers);
    }

}
