package com.bot.essay.services;

import com.bot.essay.botConfig.EssayBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Service
public class BotMainService implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final EssayBot bot;
    private final OpenAIService openAIService;
    private final RequestLimitService requestLimitService; // Добавляем зависимость для сервиса ограничения запросов

    public BotMainService(EssayBot bot, OpenAIService openAIService, RequestLimitService requestLimitService) {
        this.bot = bot;
        this.openAIService = openAIService;
        this.requestLimitService = requestLimitService;
        this.telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @Override
    public String getBotToken() {
        return bot.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String question = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            log.info("Получен вопрос: {}", question); // Логирование вопроса

            // Проверка лимита запросов
            boolean canMakeRequest = requestLimitService.checkAndUpdateRequestLimit(chatId);
            if (!canMakeRequest) {
                sendMessage(chatId, "Вы достигли лимита запросов на сегодня.");
                return; // Лимит превышен, не продолжаем обработку
            }

            // Получаем ответ от OpenAI
            String answer = openAIService.askForEssay(question); // Используем метод для сочинений

            log.info("Ответ от OpenAI: {}", answer); // Логирование ответа

            sendLongMessage(chatId, answer); // Отправка сообщения
        }
    }

    private void sendLongMessage(long chatId, String text) {
        int maxLength = 4096 * 2;
        if (text.length() > maxLength) {
            for (int i = 0; i < text.length(); i += maxLength) {
                String part = text.substring(i, Math.min(i + maxLength, text.length()));
                sendMessage(chatId, part);
            }
        } else {
            sendMessage(chatId, text);
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке сообщения: {}", e.getMessage());
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        log.info("Registered bot running state is: {}", botSession.isRunning());
    }
}

