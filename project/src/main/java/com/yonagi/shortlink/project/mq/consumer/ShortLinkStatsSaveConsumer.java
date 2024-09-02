package com.yonagi.shortlink.project.mq.consumer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Week;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yonagi.shortlink.project.common.constant.RedisKeyConstant;
import com.yonagi.shortlink.project.common.constant.ShortLinkConstant;
import com.yonagi.shortlink.project.common.convention.exception.ServerException;
import com.yonagi.shortlink.project.dao.entity.*;
import com.yonagi.shortlink.project.dao.mapper.*;
import com.yonagi.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.yonagi.shortlink.project.mq.idempotent.MessageQueueIdempotentHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接监控状态保存消息队列消费者
 * @date 2024/09/01 14:26
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = "${rocketmq.producer.topic}",
        consumerGroup = "${rocketmq.consumer.group}"
)
public class ShortLinkStatsSaveConsumer implements RocketMQListener<Map<String, String>> {

    private final ShortLinkGotoMapper shortLinkGotoMapper;
    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkLocaleStatsMapper linkLocaleStatsMapper;
    private final LinkOSStatsMapper linkOSStatsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkNetworkStatsMapper linkNetworkStatsMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final ShortLinkMapper shortLinkMapper;
    private final LinkStatsTodayMapper linkStatsTodayMapper;
    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;

    private final RedissonClient redissonClient;

    @Value("${short-link.stats.locale.amap-key}")
    private String statsLocaleAmapKey;

    @Override
    public void onMessage(Map<String, String> producerMap) {
        String keys = producerMap.get("keys");
        if (!messageQueueIdempotentHandler.isMessageProcessed(keys)) {
            // 判断当前的这个消息流程是否执行完成
            if (messageQueueIdempotentHandler.isAccomplish(keys)) {
                return;
            }
            throw new ServerException("消息未完成流程，需要消息队列重试");
        }
        try {
            String fullShortUrl = producerMap.get("fullShortUrl");
            if (StrUtil.isNotBlank(fullShortUrl)) {
                String gid = producerMap.get("gid");
                ShortLinkStatsRecordDTO statsRecord = JSON.parseObject(producerMap.get("statsRecord"), ShortLinkStatsRecordDTO.class);
                actualSaveShortLinkStats(fullShortUrl, gid, statsRecord);
            }
        } catch (Throwable ex) {
            log.error("记录短链接监控消费异常", ex);
            throw ex;
        }
        log.info("[消息访问统计监控]消息消费完成，消息Keys：{}", keys);
        messageQueueIdempotentHandler.setAccomplish(keys);
    }

    private void actualSaveShortLinkStats(String fullShortUrl, String gid, ShortLinkStatsRecordDTO statsRecord) {
        fullShortUrl = Optional.ofNullable(fullShortUrl).orElse(statsRecord.getFullShortUrl());
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(String.format(RedisKeyConstant.LOCK_GID_UPDATE_KEY, fullShortUrl));
        RLock rLock = readWriteLock.readLock();
        rLock.lock();
        try {
            if (StrUtil.isBlank(gid)) {
                LambdaQueryWrapper<ShortLinkGotoDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                        .eq(ShortLinkGotoDO::getFullShortUrl, fullShortUrl);
                ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(queryWrapper);
                gid = shortLinkGotoDO.getGid();
            }
            int hour = DateUtil.hour(new Date(), true);
            Week week = DateUtil.dayOfWeekEnum(new Date());
            int weekValue = week.getIso8601Value();
            LinkAccessStatsDO linkAccessStatsDO = LinkAccessStatsDO.builder()
                    .pv(1)
                    .uv(statsRecord.getUvFirstFlag() ? 1 : 0)
                    .uip(statsRecord.getUipFirstFlag() ? 1 : 0)
                    .hour(hour)
                    .weekday(weekValue)
                    .fullShortUrl(fullShortUrl)
                    .gid(gid)
                    .date(new Date())
                    .build();
            linkAccessStatsMapper.shortLinkStats(linkAccessStatsDO);
            Map<String, Object> localeParamMap = new HashMap<>();
            localeParamMap.put("key", statsLocaleAmapKey);
            localeParamMap.put("ip", statsRecord.getRemoteAddr());
            String localeResultStr = HttpUtil.get(ShortLinkConstant.AMAP_REMOTE_URL, localeParamMap);
            JSONObject localeResult = JSON.parseObject(localeResultStr);
            String infoCode = localeResult.getString("infocode");
            String actualProvince = "未知";
            String actualCity = "未知";
            if (StrUtil.isNotBlank(infoCode) && StrUtil.equals(infoCode, "10000")) {
                String province = localeResult.getString("province");
                boolean unknownFlag = StrUtil.equals(province, "[]");
                LinkLocaleStatsDO linkLocaleStatsDO = LinkLocaleStatsDO.builder()
                        .province(actualProvince = unknownFlag ? "未知" : province)
                        .city(actualCity = unknownFlag ? "未知" : localeResult.getString("city"))
                        .adcode(unknownFlag ? "未知" : localeResult.getString("adcode"))
                        .cnt(1)
                        .fullShortUrl(fullShortUrl)
                        .country("中国")
                        .date(new Date())
                        .build();
                linkLocaleStatsMapper.shortLinkLocaleStats(linkLocaleStatsDO);
            }
            LinkOSStatsDO linkOSStatsDO = LinkOSStatsDO.builder()
                    .os(statsRecord.getOs())
                    .cnt(1)
                    .fullShortUrl(fullShortUrl)
                    .date(new Date())
                    .build();
            linkOSStatsMapper.shortlinkOsStats(linkOSStatsDO);
            LinkBrowserStatsDO linkBrowserStatsDO = LinkBrowserStatsDO.builder()
                    .browser(statsRecord.getBrowser())
                    .cnt(1)
                    .fullShortUrl(fullShortUrl)
                    .date(new Date())
                    .build();
            linkBrowserStatsMapper.shortlinkBrowserStats(linkBrowserStatsDO);
            LinkDeviceStatsDO linkDeviceStatsDO = LinkDeviceStatsDO.builder()
                    .device(statsRecord.getDevice())
                    .cnt(1)
                    .fullShortUrl(fullShortUrl)
                    .date(new Date())
                    .build();
            linkDeviceStatsMapper.shortLinkDeviceStats(linkDeviceStatsDO);
            LinkNetworkStatsDO linkNetworkStatsDO = LinkNetworkStatsDO.builder()
                    .network(statsRecord.getNetwork())
                    .cnt(1)
                    .fullShortUrl(fullShortUrl)
                    .date(new Date())
                    .build();
            linkNetworkStatsMapper.shortLinkNetworkStats(linkNetworkStatsDO);
            LinkAccessLogsDO linkAccessLogsDO = LinkAccessLogsDO.builder()
                    .user(statsRecord.getUv())
                    .ip(statsRecord.getRemoteAddr())
                    .browser(statsRecord.getBrowser())
                    .os(statsRecord.getOs())
                    .network(statsRecord.getNetwork())
                    .device(statsRecord.getDevice())
                    .locale(StrUtil.join("-", "中国", actualProvince, actualCity))
                    .fullShortUrl(fullShortUrl)
                    .build();
            linkAccessLogsMapper.insert(linkAccessLogsDO);
            shortLinkMapper.incrementStats(gid, fullShortUrl, 1, statsRecord.getUvFirstFlag() ? 1 : 0, statsRecord.getUipFirstFlag() ? 1 : 0);
            LinkStatsTodayDO linkStatsTodayDO = LinkStatsTodayDO.builder()
                    .todayPv(1)
                    .todayUv(statsRecord.getUvFirstFlag() ? 1 : 0)
                    .todayUip(statsRecord.getUipFirstFlag() ? 1 : 0)
                    .fullShortUrl(fullShortUrl)
                    .date(new Date())
                    .build();
            linkStatsTodayMapper.shortlinkTodayStats(linkStatsTodayDO);
        } catch (Exception e) {
            log.error("短链接访问量统计异常", e);
        } finally {
            rLock.unlock();
        }
    }
}
