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

    private String formatResponse(String response) {
        if (response.contains("answer")) {
            return response;
        }

        String formattedResponse = "{\"answer\": + response.trim() +}";
        return formattedResponse;
    }
}


