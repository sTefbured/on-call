package com.stefbured.oncallserver.repository;

import com.stefbured.oncallserver.model.entity.chat.Chat;
import com.stefbured.oncallserver.model.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(value = "" +
            "select * " +
            "from chats c " +
            "join user_grants ug on c.id = ug.chat_id " +
            "join users u on ug.user_id = u.id " +
            "where u.id = ?1", nativeQuery = true)
    Page<Chat> findAllByUserId(Long userId, Pageable pageable);

    Page<Chat> findAllByGroupId(Long groupId, Pageable pageable);

    @Query(value = "" +
            "select u.id " +
            "from user_grants ug " +
            "join users u on u.id = ug.user_id " +
            "where ug.chat_id = ?1", nativeQuery = true)
    Collection<Long> findAllUserChatMembersIds(Long chatId);
}
