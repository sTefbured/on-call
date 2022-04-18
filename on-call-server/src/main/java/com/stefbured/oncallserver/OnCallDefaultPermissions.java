package com.stefbured.oncallserver;

public final class OnCallDefaultPermissions {
    public static final String DATABASE_ACCESS = "database:access";

    // User permissions
    public static final String USER_REGISTER = "user:register";
    public static final String USER_BAN = "user:ban";
    public static final String USER_KICK = "user:kick";
    public static final String MODERATOR_BAN = "moderator:ban";

    // Group permissions
    public static final String GROUP_MEMBER_VIEW = "group:memberView";
    public static final String GROUP_ADMIN_VIEW = "group:adminView";
    public static final String GROUP_CREATE = "group:create";
    public static final String GROUP_EDIT = "group:edit";
    public static final String GROUP_DELETE = "group:delete";

    private OnCallDefaultPermissions() {
    }
}
