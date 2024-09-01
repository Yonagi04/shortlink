package com.yonagi.shortlink.project.mq.idempotent;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 消息队列幂等处理
 * @date 2024/09/01 14:54
 */
@RequiredArgsConstructor
@Component
public class MessageQueueIdempotentHandler {

    private final StringRedisTemplate stringRedisTemplate;
    private static final String IDEMPOTENT_KEY_PREFIX = "short-link:idempotent:";

    /**
     * 判断当前消息是否被消费过
     * @param messageId 消息的唯一标识
     * @return 是否被消费
     */
    public boolean isMessageProcessed(String messageId) {
        String key = IDEMPOTENT_KEY_PREFIX + messageId;
        return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, "", 10, TimeUnit.MINUTES));
    }

    /**
     * 判断消息消费流程是否执行完
     * @param messageId 消息的唯一标识
     * @return 消息消费流程是否走完
     */
    public boolean isAccomplish(String messageId) {
        String key = IDEMPOTENT_KEY_PREFIX + messageId;
        return Objects.equals(stringRedisTemplate.opsForValue().get(key), "1");
    }

    /**
     * 将消息的消费流程设置为“已消费”
     * @param messageId 消息的唯一标识
     */
    public void setAccomplish(String messageId) {
        String key = IDEMPOTENT_KEY_PREFIX + messageId;
        stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.MINUTES);
    }
}
