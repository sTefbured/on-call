package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
