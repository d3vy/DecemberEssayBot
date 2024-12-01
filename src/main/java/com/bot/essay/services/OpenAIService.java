package com.bot.essay.services;

import com.bot.essay.botConfig.EssayBot;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenAIService {

    private final EssayBot bot;

    public String ask(String question) {
        try {
            OpenAiService service = new OpenAiService(bot.getOpenAIKey());

            var completionRequest = ChatCompletionRequest.builder()
                    .messages(List.of(new ChatMessage("user", question)))
                    .model("gpt-3.5-turbo")
                    .temperature(0.0)
                    .build();

            return service.createChatCompletion(completionRequest)
                    .getChoices()
                    .getFirst() // Исправлено на get(0), чтобы избежать ошибок
                    .getMessage()
                    .getContent();
        } catch (Exception e) {
            log.error("Ошибка при запросе к OpenAI: {}", e.getMessage());
            return "Произошла ошибка при генерации ответа.";
        }
    }

    public String askForEssay(String topic) {
        String prompt = """
                Тема сочинение на тему: "%s".
                """.formatted(topic);

        return ask(prompt);
    }
}

//
//Введение:
//        1.1. Определи ключевые понятия темы.
//        1.2. Кратко выскажи своё отношение к вопросу.
//        1.3. Заверши введение фразой: «В литературных произведениях можно найти немало примеров, подтверждающих мои мысли.»(Объем: 70–90 слов.)
//Первый аргумент:
//        2.1. Укажи произведение, автора и главного героя.
//                2.2. Подробно перескажи эпизод, связанный с темой, углубившись в текст произведения, чтобы показать, что ты действительно читал книгу, и сделай микровывод.(Объем: около 200 слов.)
//Второй аргумент:
//        3.1. Выбери другое произведение и другого положительного героя.
//                3.2. Перескажи соответствующий эпизод, углубляясь в детали, и сделай микровывод.(Объем: около 200 слов.)
//Вывод:
//        4.1. Подведи итог на основе приведенных аргументов.
//        4.2. Логически свяжи вывод с темой. (Объем: около 70 слов.)
//Дополнительные требования:
//Достоверность фактов:
//        1.1. Используй только реальные литературные произведения и проверенные факты.
//        1.2. Проверяй правильность написания имен героев, названий произведений и событий.
//Правильность написания:
//        2.1. Проверь орфографию и пунктуацию имен собственных, названий и терминов.
//Отсутствие повторов:
//        3.1. Избегай повторов слов и выражений.
//                3.2. Используй разнообразие формулировок для сохранения стилистического богатства.
//Связность и логика:
//        4.1. Соблюдай логическую последовательность изложения.
//        4.2. Каждый аргумент должен содержать конкретный эпизод и обоснованный вывод.
//Литературный стиль:
//        5.1. Используй литературный язык, избегая избыточных деталей.
//                5.2. Обеспечь плавные переходы между частями текста.
//Финальная проверка:
//        6.1. Проверь соответствие всем требованиям: точность фактов, логичность и грамотность.
//        6.2. Убедись в структурированности и соблюдении всех объемов.
//
