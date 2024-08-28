package com.yonagi.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yonagi.shortlink.admin.common.convention.result.Result;
import com.yonagi.shortlink.admin.common.convention.result.Results;
import com.yonagi.shortlink.admin.remote.ShortLinkRemoteService;
import com.yonagi.shortlink.admin.remote.dto.req.ShortLinkGroupStatsReqDTO;
import com.yonagi.shortlink.admin.remote.dto.req.ShortLinkStatsAccessRecordReqDTO;
import com.yonagi.shortlink.admin.remote.dto.req.ShortLinkStatsReqDTO;
import com.yonagi.shortlink.admin.remote.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import com.yonagi.shortlink.admin.remote.dto.resp.ShortLinkStatsRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接监控控制层
 * @date 2024/08/27 20:38
 */
@RestController
public class ShortLinkStatsController {

    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};

    /**
     * 访问单个短链接指定时间内的监控数据
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/stats")
    public Result<ShortLinkStatsRespDTO> shortLinkStats(ShortLinkStatsReqDTO requestParam) {
        return Results.success(shortLinkRemoteService.oneShortLinkStats(requestParam));
    }

    /**
     * 访问分组短链接指定时间内的监控数据
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/stats/group")
    public Result<ShortLinkStatsRespDTO> groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam) {
        return Results.success(shortLinkRemoteService.groupShortLinkStats(requestParam));
    }

    /**
     * 访问单个短链接指定时间内访问记录的监控数据
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/v1/stats/access-record")
    public Result<IPage<ShortLinkStatsAccessRecordRespDTO>> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam) {
        return Results.success(shortLinkRemoteService.oneShortLinkStatsAccessRecord(requestParam));
    }
}
