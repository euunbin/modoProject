package com.example.modoproject.Chat;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatgptService chatgptService;

    public String getChatResponse(String prompt) {
        String formattedPrompt = prompt + " 다음 형식으로 대답해 주세요: {\"answer\": }";
        String response = chatgptService.sendMessage(formattedPrompt);
        return formatResponse(response);
    }

    public String getSummaryResponse(String reviews) {
        String prompt = """
    
    고객들에게 가게의 후기를 요약하는 기능입니다.
    한글로 대답해야 하며, 존댓말을 사용해야 합니다.
    한국의 맞춤법을 지켜주세요.
    앞뒤 문맥이 자연스럽게 연결되도록 작성해 주세요.
    반드시 '해당 가게는 ~'으로 시작하며, '입니다.'로 끝나는 문장으로 요약해 주세요.
    리뷰에 언급된 내용이 있다면, 도시락의 품질, 배달 시간, 고객 서비스와 관련된 사항을 포함해 주세요.
    공백을 포함해 200자 이내로 답변해 주세요.
    너무 길거나 불필요한 정보는 빼주세요.
    리뷰로부터 언급되지 않은 내용은 작성하지 말아주세요.

    예시: 
    "해당 가게는 도시락이 신선하고 배달이 빠르며 고객 서비스가 친절합니다."
    
    요약할 리뷰들:
    %s
    
    """.formatted(reviews);

        String response = getChatResponse(prompt);
        return response;
    }



    private String formatResponse(String response) {
        if (response.contains("answer")) {
            return response;
        }

        return "{\"answer\": \"" + response.trim() + "\"}";
    }

}
