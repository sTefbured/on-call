package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}