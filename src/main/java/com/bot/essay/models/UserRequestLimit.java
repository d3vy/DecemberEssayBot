package com.bot.essay.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "user_request_limits")
public class UserRequestLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId; // Идентификатор пользователя, внешний ключ на таблицу customers

    @Column(name = "requests_today", nullable = false)
    private int requestsToday = 0; // Количество запросов за сегодня

    @Column(name = "last_request_date", nullable = false)
    private LocalDate lastRequestDate; // Дата последнего запроса

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private Customer customer; // Связь с таблицей customers
}
