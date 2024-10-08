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
    public static final String GOTO_SHORT_LINK_KEY = "short-link:goto:%s";

    /**
     * 短链接空值跳转key前缀
     */
    public static final String GOTO_IS_NULL_SHORT_LINK_KEY = "short-link:is-null:goto_%s";

    /**
     * 短链接跳转分布式锁对应key
     */
    public static final String LOCK_GOTO_SHORT_LINK_KEY = "short-link:lock:goto:%s";

    /**
     * 短链接统计独立访客对应key
     */
    public static final String UV_SHORT_LINK_KEY = "short-link:stats:uv:";

    /**
     * 短链接统计独立ip对应key
     */
    public static final String UIP_SHORT_LINK_KEY = "short-link:stats:uip:";

    /**
     * 短链接修改分组 ID 锁前缀 Key
     */
    public static final String LOCK_GID_UPDATE_KEY = "short-link:lock:update-gid:%s";

    /**
     * 短链接延迟队列消费统计 Key
     */
    public static final String DELAY_QUEUE_STATS_KEY = "short-link:delay-queue:stats";
}
