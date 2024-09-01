package com.yonagi.shortlink.admin.common.constant;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description Redis 常量类
 * @date 2024/08/14 13:31
 */
public class RedisCacheConstant {

    /**
     * 用户注册锁
     */
    public final static String LOCK_USER_REGISTER_KEY = "short-link:lock_user-register:";

    /**
     * 用户创建分组锁
     */
    public final static String LOCK_SAVE_GROUP_KEY = "short-link:lock_save-group:%s";

    /**
     * 用户登录key
     */
    public final static String USER_LOGIN_KEY = "short-link:login:";
}
