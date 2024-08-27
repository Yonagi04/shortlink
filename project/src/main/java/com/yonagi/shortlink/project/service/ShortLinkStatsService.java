package com.yonagi.shortlink.project.service;

import com.yonagi.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkStatsRespDTO;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接监控服务层
 * @date 2024/08/27 10:50
 */
public interface ShortLinkStatsService {

    /**
     * 获取单个短链接指定时间段内监控数据
     * @param requestParam
     * @return
     */
    ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam);
}
