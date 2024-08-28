package com.yonagi.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yonagi.shortlink.project.common.convention.exception.ClientException;
import com.yonagi.shortlink.project.dao.entity.*;
import com.yonagi.shortlink.project.dao.mapper.*;
import com.yonagi.shortlink.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.yonagi.shortlink.project.dto.resp.*;
import com.yonagi.shortlink.project.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接监控服务层实现
 * @date 2024/08/27 10:56
 */
@Service
@RequiredArgsConstructor
public class ShortLinkStatsServiceImpl implements ShortLinkStatsService {

    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkLocaleStatsMapper linkLocaleStatsMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkOSStatsMapper linkOSStatsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkNetworkStatsMapper linkNetworkStatsMapper;

    @Override
    public ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        // 基础访问详情
        List<LinkAccessStatsDO> listStatsByShortLink = linkAccessStatsMapper.listStatsByShortLink(requestParam);
        if (listStatsByShortLink == null || CollUtil.isEmpty(listStatsByShortLink)) {
            return null;
        }
        LinkAccessStatsDO uvPvUipStatsByShortLink = linkAccessLogsMapper.findUvPvUipStatsByShortLink(requestParam);
        // 地区访问详情
        List<ShortLinkStatsLocaleCNRespDTO> localeCnStats = new ArrayList<>();
        List<LinkLocaleStatsDO> listLocaleByShortLink = linkLocaleStatsMapper.listLocaleByShortLink(requestParam);
        int localeCnSum = listLocaleByShortLink.stream()
                .mapToInt(LinkLocaleStatsDO::getCnt)
                .sum();
        listLocaleByShortLink.forEach(each -> {
            double ratio = (double) each.getCnt() / localeCnSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortLinkStatsLocaleCNRespDTO localeCNRespDTO = ShortLinkStatsLocaleCNRespDTO.builder()
                    .cnt(each.getCnt())
                    .locale(each.getProvince())
                    .ratio(actualRatio)
                    .build();
            localeCnStats.add(localeCNRespDTO);
        });
        // 小时访问详情
        List<Integer> hoursStats = new ArrayList<>();
        List<LinkAccessStatsDO> listHourStatsByShortLink = linkAccessStatsMapper.listHourStatsByShortLink(requestParam);
        for (int i = 0; i < 24; i++) {
            AtomicInteger hour = new AtomicInteger();
            Integer hourCnt = listHourStatsByShortLink.stream()
                    .filter(each -> Objects.equals(each.getHour(), hour.get()))
                    .findFirst()
                    .map(LinkAccessStatsDO::getPv)
                    .orElse(0);
            hoursStats.add(hourCnt);
        }
        // 高频访问ip详情
        List<ShortLinkStatsTopIpRespDTO> topIpStats = new ArrayList<>();
        List<HashMap<String, Object>> listTopIpByShortLink = linkAccessLogsMapper.listTopIpByShortLink(requestParam);
        listTopIpByShortLink.forEach(each -> {
            ShortLinkStatsTopIpRespDTO shortLinkStatsTopIpRespDTO = ShortLinkStatsTopIpRespDTO.builder()
                    .ip(each.get("ip").toString())
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .build();
            topIpStats.add(shortLinkStatsTopIpRespDTO);
        });
        // 一周访问详情
        List<Integer> weekdays = new ArrayList<>();
        List<LinkAccessStatsDO> listWeekdayStatsByShortLink = linkAccessStatsMapper.listWeekdayStatsByShortLink(requestParam);
        for (int i = 0; i < 8; i++) {
            AtomicInteger weekday = new AtomicInteger();
            Integer weekdayCnt = listWeekdayStatsByShortLink.stream()
                    .filter(each -> Objects.equals(each.getWeekday(), weekday.get()))
                    .findFirst()
                    .map(LinkAccessStatsDO::getPv)
                    .orElse(0);
            weekdays.add(weekdayCnt);
        }
        // 浏览器访问详情
        List<ShortLinkStatsBrowserRespDTO> browserStats = new ArrayList<>();
        List<HashMap<String, Object>> listBrowserStatsByShortLink = linkBrowserStatsMapper.listBrowserStatsByShortLink(requestParam);
        int browserSum = listBrowserStatsByShortLink.stream()
                .mapToInt(each -> Integer.parseInt(each.get("count").toString()))
                .sum();
        listBrowserStatsByShortLink.forEach(each -> {
            double ratio = (double) Integer.parseInt(each.get("count").toString()) / browserSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortLinkStatsBrowserRespDTO shortLinkStatsBrowserRespDTO = ShortLinkStatsBrowserRespDTO.builder()
                    .browser(each.get("browser").toString())
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .ratio(actualRatio)
                    .build();
            browserStats.add(shortLinkStatsBrowserRespDTO);
        });
        // 操作系统访问详情
        List<ShortLinkStatsOsRespDTO> osStats = new ArrayList<>();
        List<HashMap<String, Object>> listOsStatsByShortLink = linkOSStatsMapper.listOsStatsByShortLink(requestParam);
        int osSum = listOsStatsByShortLink.stream()
                .mapToInt(each -> Integer.parseInt(each.get("count").toString()))
                .sum();
        listOsStatsByShortLink.forEach(each -> {
            double ratio = (double) Integer.parseInt(each.get("count").toString()) / browserSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortLinkStatsOsRespDTO shortLinkStatsOsRespDTO = ShortLinkStatsOsRespDTO.builder()
                    .os(each.get("os").toString())
                    .cnt(Integer.parseInt(each.get("count").toString()))
                    .ratio(actualRatio)
                    .build();
            osStats.add(shortLinkStatsOsRespDTO);
        });
        // 访客访问类型详情
        List<ShortLinkStatsUvRespDTO> uvTypeStats = new ArrayList<>();
        HashMap<String, Object> uvTypeCntByShortLink = linkAccessLogsMapper.findUvTypeCntByShortLink(requestParam);
        int oldUserCnt = Integer.parseInt(uvTypeCntByShortLink.get("oldUserCnt").toString());
        int newUserCnt = Integer.parseInt(uvTypeCntByShortLink.get("newUserCnt").toString());
        int uvSum = oldUserCnt + newUserCnt;
        double oldRatio = (double) oldUserCnt / uvSum;
        double actualOldRatio = Math.round(oldRatio * 100.0) / 100.0;
        double newRatio = (double) newUserCnt / uvSum;
        double actualNewRatio = Math.round(newRatio * 100.0) / 100.0;
        ShortLinkStatsUvRespDTO newUvRespDTO = ShortLinkStatsUvRespDTO.builder()
                .uvType("newUser")
                .ratio(actualNewRatio)
                .cnt(newUserCnt)
                .build();
        uvTypeStats.add(newUvRespDTO);
        ShortLinkStatsUvRespDTO oldUvRespDTO = ShortLinkStatsUvRespDTO.builder()
                .uvType("oldUser")
                .ratio(actualOldRatio)
                .cnt(oldUserCnt)
                .build();
        uvTypeStats.add(oldUvRespDTO);
        // 访问设备类型详情
        List<ShortLinkStatsDeviceRespDTO> deviceTypeStats = new ArrayList<>();
        List<LinkDeviceStatsDO> listDeviceStatsByShortLink = linkDeviceStatsMapper.listDeviceStatsByShortLink(requestParam);
        int deviceSum = listDeviceStatsByShortLink.stream()
                .mapToInt(LinkDeviceStatsDO::getCnt)
                .sum();
        listDeviceStatsByShortLink.forEach(each -> {
            double ratio = (double) each.getCnt() / deviceSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortLinkStatsDeviceRespDTO shortLinkStatsDeviceRespDTO = ShortLinkStatsDeviceRespDTO.builder()
                    .ratio(actualRatio)
                    .device(each.getDevice())
                    .cnt(each.getCnt())
                    .build();
            deviceTypeStats.add(shortLinkStatsDeviceRespDTO);
        });
        // 访问网络类型详情
        List<ShortLinkStatsNetworkRespDTO> networkTypeStats = new ArrayList<>();
        List<LinkNetworkStatsDO> listNetworkStatsByShortLink = linkNetworkStatsMapper.listNetworkStatsByShortLink(requestParam);
        int networkSum = listNetworkStatsByShortLink.stream()
                .mapToInt(LinkNetworkStatsDO::getCnt)
                .sum();
        listNetworkStatsByShortLink.forEach(each -> {
            double ratio = (double) each.getCnt() / networkSum;
            double actualRatio = Math.round(ratio * 100.0) / 100.0;
            ShortLinkStatsNetworkRespDTO shortLinkStatsNetworkRespDTO = ShortLinkStatsNetworkRespDTO.builder()
                    .ratio(actualRatio)
                    .network(each.getNetwork())
                    .cnt(each.getCnt())
                    .build();
            networkTypeStats.add(shortLinkStatsNetworkRespDTO);
        });

        return ShortLinkStatsRespDTO.builder()
                .daily(BeanUtil.copyToList(listStatsByShortLink, ShortLinkStatsAccessDailyRespDTO.class))
                .pv(uvPvUipStatsByShortLink.getPv())
                .uv(uvPvUipStatsByShortLink.getUv())
                .uip(uvPvUipStatsByShortLink.getUip())
                .localeCnStats(localeCnStats)
                .hourStats(hoursStats)
                .topIpStats(topIpStats)
                .weekdayStats(weekdays)
                .browserStats(browserStats)
                .osStats(osStats)
                .uvTypeStats(uvTypeStats)
                .deviceStats(deviceTypeStats)
                .networkStats(networkTypeStats)
                .build();
    }

    @Override
    public IPage<ShortLinkStatsAccessRecordRespDTO> oneShortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam) {
        if (requestParam.getStartDate() == null || StrUtil.isEmpty(requestParam.getStartDate()) || requestParam.getEndDate() == null || StrUtil.isEmpty(requestParam.getEndDate())) {
            throw new ClientException("缺少开始和结束时间");
        }
        LambdaQueryWrapper<LinkAccessLogsDO> queryWrapper = Wrappers.lambdaQuery(LinkAccessLogsDO.class)
                .eq(LinkAccessLogsDO::getGid, requestParam.getGid())
                .eq(LinkAccessLogsDO::getFullShortUrl, requestParam.getFullShortUrl())
                .between(LinkAccessLogsDO::getCreateTime, requestParam.getStartDate(), requestParam.getEndDate())
                .eq(LinkAccessLogsDO::getDelFlag, 0)
                .orderByDesc(LinkAccessLogsDO::getCreateTime);
        IPage<LinkAccessLogsDO> resultPage = linkAccessLogsMapper.selectPage(requestParam, queryWrapper);
        IPage<ShortLinkStatsAccessRecordRespDTO> actualResult = resultPage.convert(each -> BeanUtil.toBean(each, ShortLinkStatsAccessRecordRespDTO.class));
        List<String> userAccessLogsList = actualResult.getRecords()
                .stream()
                .map(ShortLinkStatsAccessRecordRespDTO::getUser)
                .toList();
        if (CollUtil.isEmpty(userAccessLogsList)) {
            return actualResult;
        }
        List<Map<String, Object>> uvTypeList = linkAccessLogsMapper.selectUvTypeByUser(
                requestParam.getGid(),
                requestParam.getFullShortUrl(),
                requestParam.getStartDate(),
                requestParam.getEndDate(),
                userAccessLogsList
        );
        actualResult.getRecords().forEach(each -> {
            String uvType = uvTypeList.stream()
                    .filter(item -> Objects.equals(each.getUser(), item.get("user")))
                    .findFirst()
                    .map(item -> item.get("uvType"))
                    .map(Object::toString)
                    .orElse("旧访客");
            each.setUvType(uvType);
        });
        return actualResult;
    }
}
