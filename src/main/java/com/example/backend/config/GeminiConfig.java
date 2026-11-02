// package com.example.backend.config;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import com.google.genai.Client;

// @Configuration
// public class GeminiConfig {

//     @Bean
//     public Client geminiClient() {
//         String apiKey = "".trim();

//         return Client.builder()
//                 .apiKey(apiKey)
//                 .build();
//     }
// }
package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.genai.Client;

@Configuration
public class GeminiConfig {

    @Bean
    public Client geminiClient() {

        String apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("GEMINI_API_KEY is not configured");
        }

        return Client.builder().apiKey(apiKey.trim()).build();
    }
}