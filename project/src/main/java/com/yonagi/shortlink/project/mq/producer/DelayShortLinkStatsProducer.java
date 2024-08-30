package com.yonagi.shortlink.project.mq.producer;

import com.yonagi.shortlink.project.common.constant.RedisKeyConstant;
import com.yonagi.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 延迟记录短链接统计数据组件
 * @date 2024/08/30 10:33
 */
@Component
@RequiredArgsConstructor
public class DelayShortLinkStatsProducer {

    private final RedissonClient redissonClient;

    /**
     * 发送延迟消费短链接数据统计
     * @param shortLinkStatsRecordDTO
     */
    public void send(ShortLinkStatsRecordDTO shortLinkStatsRecordDTO) {
        RBlockingDeque<Object> blockingDeque = redissonClient.getBlockingDeque(RedisKeyConstant.DELAY_QUEUE_STATS_KEY);
        RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
        delayedQueue.offer(shortLinkStatsRecordDTO, 5, TimeUnit.SECONDS);
    }
}
