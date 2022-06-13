package com.stefbured.oncallserver.service.impl;

import com.stefbured.oncallserver.exception.EmptyMessageException;
import com.stefbured.oncallserver.model.entity.chat.Message;
import com.stefbured.oncallserver.repository.MessageRepository;
import com.stefbured.oncallserver.service.ChatService;
import com.stefbured.oncallserver.service.MessageService;
import com.stefbured.oncallserver.service.NotificationService;
import com.stefbured.oncallserver.utils.LongPrimaryKeyGenerator;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.naming.LimitExceededException;
import java.time.LocalDateTime;
import java.util.Collection;

import static com.stefbured.oncallserver.OnCallConstants.NotificationTypes.MESSAGE;

@Service
public class MessageServiceImpl implements MessageService {
    private static final int MAX_NOTIFICATION_TEXT_LENGTH = 30;

    private final NotificationService notificationService;
    private final ChatService chatService;
    private final MessageRepository messageRepository;
    private final LongPrimaryKeyGenerator primaryKeyGenerator;

    @Autowired
    public MessageServiceImpl(NotificationService notificationService,
                              ChatService chatService,
                              MessageRepository messageRepository,
                              LongPrimaryKeyGenerator primaryKeyGenerator) {
        this.notificationService = notificationService;
        this.chatService = chatService;
        this.messageRepository = messageRepository;
        this.primaryKeyGenerator = primaryKeyGenerator;
    }

    @Override
    public Message createMessage(Message message) {
        var messageText = message.getText();
        checkMessageTextNotEmpty(messageText);
        messageText = messageText.trim();
        checkMessageTextNotEmpty(messageText);
        message.setText(messageText);
        message.setId(primaryKeyGenerator.generatePk(Message.class));
        message.setSendingDateTime(LocalDateTime.now());
        var result = messageRepository.save(message);

        var targetUserIds = chatService.getAllChatMembersIds(result.getChat());
        targetUserIds.remove(message.getSender().getId());
        var notificationText = result.getText().length() > MAX_NOTIFICATION_TEXT_LENGTH
                ? result.getText().substring(0, MAX_NOTIFICATION_TEXT_LENGTH - 3) + "..."
                : result.getText();
        targetUserIds.forEach(userId -> {
            var senderId = message.getSender().getId();
            notificationService.createNotification(senderId, message.getChat().getId(), notificationText, MESSAGE, userId);
        });
        return result;
    }

    private void checkMessageTextNotEmpty(String messageText) {
        if (Strings.isEmpty(messageText)) {
            throw new EmptyMessageException("Message is empty. Operation aborted.");
        }
    }

    @Override
    public Message getMessageById(Long messageId) {
        return messageRepository.findById(messageId).orElseThrow();
    }

    @Override
    public Collection<Message> getMessages(Long chatId, int page, int size) throws LimitExceededException {
        var messagesCount = messageRepository.countMessagesByChatId(chatId);
        if ((long) page * size + 1 > messagesCount) {
            throw new LimitExceededException();
        }
        return messageRepository.findAllByChatIdOrderBySendingDateTimeDesc(chatId, Pageable.ofSize(size).withPage(page));
    }
}
