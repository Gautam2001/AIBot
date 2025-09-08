package com.AIBot.Service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.AIBot.DTO.MessageDTO;
import com.AIBot.Service.HuggingFaceService;

@Service
public class HuggingFaceServiceImpl implements HuggingFaceService {

    @Value("${HF_API_KEY}")
    private String apiKey;

    @Value("${HF_API_URL}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String chat(List<MessageDTO> history, String userMessage) {
        // Build messages JSON (system + history + user)
        List<Map<String, String>> messages = new ArrayList<>();

        // System message to disable "think"
        Map<String, String> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", "/no_think");
        messages.add(systemMsg);

        // Add chat history
        if (history != null) {
            for (MessageDTO msg : history) {
                Map<String, String> entry = new HashMap<>();
                entry.put("role", msg.getRole());
                entry.put("content", msg.getContent());
                messages.add(entry);
            }
        }

        // Add current user message
        Map<String, String> current = new HashMap<>();
        current.put("role", "user");
        current.put("content", userMessage);
        messages.add(current);

        // Build request body
        Map<String, Object> body = new HashMap<>();
        body.put("model", "HuggingFaceTB/SmolLM3-3B:hf-inference");
        body.put("messages", messages);
        body.put("stream", false);

        // Sampling parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("temperature", 0.6);  // creativity / randomness
        parameters.put("top_p", 0.95);       // nucleus sampling
        body.put("parameters", parameters);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, requestEntity, Map.class);

            // Extract the text response
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return message.get("content").toString();
            }
            return "No response from model.";
        } catch (Exception e) {
            throw new RuntimeException("Hugging Face error: " + e.getMessage(), e);
        }
    }
}
