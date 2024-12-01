package com.bot.essay.repositories;

import com.bot.essay.models.UserRequestLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRequestLimitRepository extends JpaRepository<UserRequestLimit, Long> {
}
