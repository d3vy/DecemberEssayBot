package com.bot.essay.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId; // Идентификатор пользователя

    @Column(name = "chat_id", nullable = false)
    private Long chatId; // chat_id пользователя

    @Column(name = "username")
    private String username; // Имя пользователя

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private UserRequestLimit userRequestLimit; // Ссылка на сущность UserRequestLimit

    //Переопределение метода toString().
    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", name='" + username + '\'' +
                '}';
    }
}
