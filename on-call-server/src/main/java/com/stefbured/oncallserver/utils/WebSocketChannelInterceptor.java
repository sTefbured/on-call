package com.stefbured.oncallserver.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stefbured.oncallserver.service.UserService;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Objects;
import java.util.regex.Pattern;

import static com.stefbured.oncallserver.OnCallConstants.MESSAGE_SEND;

@Component
public class WebSocketChannelInterceptor implements ChannelInterceptor {
    private static final String SEND_CHAT_MESSAGE_DESTINATION = "/app/message";
    private static final Pattern USER_NOTIFICATIONS_DESTINATION = Pattern.compile("/user/\\d+/notification");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

    private UserService userService;

    @Override
    @Transactional
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        var headerAccessor = StompHeaderAccessor.wrap(message);
        switch (Objects.requireNonNull(headerAccessor.getCommand())) {
            case SEND -> {
                if (isSendOperationAccessible(headerAccessor, message)) {
                    return message;
                }
            }
            case SUBSCRIBE -> {
                if (isSubscriptionPossible(headerAccessor)) {
                    return message;
                }
            }
            default -> {
                return message;
            }
        }
        throw new AccessDeniedException("Access denied");
    }

    @SneakyThrows
    private boolean isSubscriptionPossible(StompHeaderAccessor headerAccessor) {
        var destination = headerAccessor.getDestination();
        if (destination == null) {
            return true;
        }
        var user = headerAccessor.getUser();
        if (USER_NOTIFICATIONS_DESTINATION.matcher(destination).matches()) {
            var userId = userService.getUserIdByUsername(Objects.requireNonNull(user).getName());
            var matcher = NUMBER_PATTERN.matcher(destination);
            if (matcher.find()) {
                var pathId = Long.parseLong(matcher.group());
                return userId.equals(pathId);
            }
            throw new AccessDeniedException("Access denied");
        }
        return true;
    }

    private boolean isSendOperationAccessible(StompHeaderAccessor headerAccessor, Message<?> message) {
        var destination = headerAccessor.getDestination();
        if (destination == null) {
            return true;
        }
        var user = headerAccessor.getUser();
        if (SEND_CHAT_MESSAGE_DESTINATION.equals(destination)) {
            return isUserAbleToSendMessageToChat(user, message);
        }
        return true;
    }

    @SneakyThrows
    public boolean isUserAbleToSendMessageToChat(Principal user, Message<?> message) {
        var userId = userService.getUserIdByUsername(Objects.requireNonNull(user).getName());
        var mapper = new ObjectMapper();
        var payload = new String((byte[]) message.getPayload());
        var chatId = mapper.readTree(payload).findValue("chat").findValue("id").asLong();
        var chat = OnCallUtils.getChatService().getChatById(chatId);
        if (chat.getGroup() != null) {
            return OnCallUtils.getGroupService().isUserMemberOfGroup(userId, chat.getGroup().getId());
        }
        return userService.userHasAuthorityForChat(userId, chatId, MESSAGE_SEND);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}