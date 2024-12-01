package com.bot.essay.botConfig;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Data
public class EssayBot {
    @Value("${bot.name}")
    private String name;
    @Value("${bot.token}")
    private String token;
    @Value("${bot.api-key}")
    private String openAIKey;
}
