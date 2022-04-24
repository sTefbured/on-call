package com.stefbured.oncallserver;

public final class OnCallConstants {
    public static final long ON_CALL_USER_ROLE_ID = 345765242562356L;


    public static final String DATABASE_ACCESS = "database:access";

    // User permissions
    public static final String USER_REGISTER = "user:register";
    public static final String USER_BAN = "user:ban";
    public static final String USER_PUBLIC_INFO_VIEW = "user:publicInfoView";
    public static final String USER_PRIVATE_INFO_VIEW = "user:privateInfoView";
    public static final String USER_EDIT = "user:edit";
    public static final String MODERATOR_BAN = "moderator:ban";

    // Group permissions
    public static final String GROUP_MEMBER_VIEW = "group:memberView";
    public static final String GROUP_ADMIN_VIEW = "group:adminView";
    public static final String GROUP_CREATE = "group:create";
    public static final String GROUP_EDIT = "group:edit";
    public static final String GROUP_DELETE = "group:delete";

    // UserGrant permissions
    public static final String USER_GRANT_CREATE = "userGrant:create";
    public static final String USER_GRANT_VIEW = "userGrant:view";
    public static final String USER_GRANT_DELETE = "userGrant:delete";

    // ScheduleRecord permissions
    public static final String SCHEDULE_RECORD_CREATE = "scheduleRecord:create";
    public static final String SCHEDULE_RECORD_VIEW = "scheduleRecord:view";
    public static final String SCHEDULE_RECORD_EDIT = "scheduleRecord:edit";
    public static final String SCHEDULE_RECORD_DELETE = "scheduleRecord:delete";

    // Roles
    public static final Long ON_CALL_ADMINISTRATOR = 784532567455345L;
    public static final Long ON_CALL_USER = 345765242562356L;
    public static final Long GROUP_ADMINISTRATOR = 335652845749234L;
    public static final Long GROUP_MEMBER = 444562356788634L;

    private OnCallConstants() {
    }
}
