package com.stefbured.oncallserver.utils;

import com.stefbured.oncallserver.service.ChatService;
import com.stefbured.oncallserver.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OnCallUtils {
    private static OnCallUtils instance;

    private ChatService chatService;
    private GroupService groupService;

    public static ChatService getChatService() {
        return instance.chatService;
    }

    public static GroupService getGroupService() {
        return instance.groupService;
    }

    private static void setOnCallUtilsInstance(OnCallUtils onCallUtils) {
        instance = onCallUtils;
    }

    @Autowired
    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    @Autowired
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    @Autowired
    public void setInstance() {
        OnCallUtils.setOnCallUtilsInstance(this);
    }
}
