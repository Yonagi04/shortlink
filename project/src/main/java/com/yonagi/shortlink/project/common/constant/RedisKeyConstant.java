package com.yonagi.shortlink.project.common.constant;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description Redis常量类
 * @date 2024/08/18 15:56
 */
public class RedisKeyConstant {

    /**
     * 短链接跳转key前缀
     */
    public static final String GOTO_SHORT_LINK_KEY = "short-link_goto_%s";

    /**
     * 短链接空值跳转key前缀
     */
    public static final String GOTO_IS_NULL_SHORT_LINK_KEY = "short-link_is-null_goto_%s";

    /**
     * 短链接跳转分布式锁对应key
     */
    public static final String LOCK_GOTO_SHORT_LINK_KEY = "short-link_lock_goto_%s";

    /**
     * 短链接统计独立访客对应key
     */
    public static final String UV_SHORT_LINK_KEY = "short-link:stats:uv:";

    /**
     * 短链接统计独立ip对应key
     */
    public static final String UIP_SHORT_LINK_KEY = "short-link:stats:uip:";
}
