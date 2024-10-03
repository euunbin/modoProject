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
        한글로 대답해야하며, 존댓말을 사용해야 합니다.
        한국의 맞춤법을 지켜주세요.
        앞 뒤 문맥이 매끄럽게 이어지도록 답변해주세요.
        해당 가게는 ~ 으로 문장을 시작해야하며, ~니다. 말투로 마무리해야합니다.
        위와 같은 조건으로 리뷰들을 "공백 제외 100자" 이내로 요약해 주세요:
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
