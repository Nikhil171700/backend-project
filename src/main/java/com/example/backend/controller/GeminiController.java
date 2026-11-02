
package com.example.backend.controller;

import com.example.backend.service.GeminiService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/legacy-chat")
    public Map<String, String> chat(@RequestBody Map<String, String> request) {

        String prompt = request.get("prompt");

        String response = geminiService.generateResponse(prompt);

        return Map.of("response", response);
    }
}
