package com.bot.essay.services;

import com.bot.essay.models.Customer;
import com.bot.essay.models.UserRequestLimit;
import com.bot.essay.repositories.CustomerRepository;
import com.bot.essay.repositories.UserRequestLimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestLimitService {
    private final UserRequestLimitRepository userRequestLimitRepositoryRepository;
    private final CustomerRepository customerRepositoryRepository;

    public boolean checkAndUpdateRequestLimit(Long userId) {
        Optional<UserRequestLimit> limitOptional = userRequestLimitRepositoryRepository.findById(userId);

        if (limitOptional.isPresent()) {
            UserRequestLimit limit = limitOptional.get();

            // Если дата запроса изменилась, сбрасываем счетчик запросов
            if (!limit.getLastRequestDate().equals(LocalDate.now())) {
                limit.setRequestsToday(0);
                limit.setLastRequestDate(LocalDate.now());
            }

            // Проверяем, не достиг ли лимит
            if (limit.getRequestsToday() >= 10) {
                return false; // Лимит превышен
            }

            // Обновляем количество запросов
            limit.setRequestsToday(limit.getRequestsToday() + 1);
            userRequestLimitRepositoryRepository.save(limit);
            return true;
        }

        // Если запись не найдена, создаем новую
        UserRequestLimit newLimit = new UserRequestLimit();
        Optional<Customer> customerOptional = customerRepositoryRepository.findById(userId);
        if (customerOptional.isPresent()) {
            newLimit.setCustomer(customerOptional.get());
            newLimit.setRequestsToday(1);
            newLimit.setLastRequestDate(LocalDate.now());
            userRequestLimitRepositoryRepository.save(newLimit);
            return true;
        }

        return false; // Пользователь не найден
    }
}
