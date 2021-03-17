package com.kuke.videomeeting.config.cache;

public class CacheKey {
    public static final int DEFAULT_EXPIRE_SEC = 60; // seconds
    public static final String USER = "user";
    public static final int USER_EXPIRE_SEC = 60 * 5;
    public static final String USERS = "users";
    public static final int USERS_EXPIRE_SEC = 60 * 2;
    public static final String USER_DETAILS = "user_details";
    public static final int USER_DETAILS_EXPIRE_SEC = 60 * 10;
    public static final String FRIENDS = "friends";
    public static final int FRIENDS_EXPIRE_SEC = 60 * 5;
    public static final String SENT_MESSAGES = "sent_messages";
    public static final int SENT_MESSAGES_EXPIRE_SEC = 60 * 5;
    public static final String RECEIVED_MESSAGES = "received_messages";
    public static final int RECEIVED_MESSAGES_EXPIRE_SEC = 60 * 5;

    public static final String CODE = "code";
    public static final int CODE_EXPIRE_SEC = 60 * 5;


}
