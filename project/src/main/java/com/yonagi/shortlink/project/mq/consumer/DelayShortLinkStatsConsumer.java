package com.yonagi.shortlink.project.mq.consumer;

import com.yonagi.shortlink.project.common.constant.RedisKeyConstant;
import com.yonagi.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.yonagi.shortlink.project.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 延迟记录短链接统计数据组件
 * @date 2024/08/30 10:24
 */
@Component
@RequiredArgsConstructor
public class DelayShortLinkStatsConsumer implements InitializingBean {

    private final RedissonClient redissonClient;
    private final ShortLinkService shortLinkService;

    public void onMessage() {
        Executors.newSingleThreadExecutor(
                runable -> {
                    Thread thread = new Thread();
                    thread.setName("delay_short-link_stats_consumer");
                    thread.setDaemon(Boolean.TRUE);
                    return thread;
                }
        ).execute(() -> {
            RBlockingDeque<ShortLinkStatsRecordDTO> blockingDeque = redissonClient.getBlockingDeque(RedisKeyConstant.DELAY_QUEUE_STATS_KEY);
            RDelayedQueue<ShortLinkStatsRecordDTO> delayedQueue = redissonClient.getDelayedQueue(blockingDeque);
            for (; ;) {
                try {
                    ShortLinkStatsRecordDTO shortLinkStatsRecordDTO = delayedQueue.poll();
                    if (shortLinkStatsRecordDTO != null) {
                        shortLinkService.shortLinkStats(null, null, shortLinkStatsRecordDTO);
                        continue;
                    }
                    LockSupport.parkUntil(500);
                } catch (Throwable ignored) {

                }
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        onMessage();
    }
}
