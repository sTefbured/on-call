package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.chat.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Collection<Message> getAllByChatId(Long chatId, Pageable pageable);
}
