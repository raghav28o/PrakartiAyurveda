package com.PrakartiAyurVeda.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OpenAIConfig {

    @Bean
    @Primary
    public ChatClient chatClient(OpenAiChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4-turbo")
                        .temperature(0.7)
                        .build())
                .build();
    }
}
