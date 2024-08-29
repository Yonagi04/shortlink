package com.yonagi.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yonagi.shortlink.admin.common.convention.result.Result;
import com.yonagi.shortlink.admin.common.convention.result.Results;
import com.yonagi.shortlink.admin.remote.ShortLinkRemoteService;
import com.yonagi.shortlink.admin.remote.dto.req.ShortLinkBatchCreateReqDTO;
import com.yonagi.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.yonagi.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.yonagi.shortlink.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import com.yonagi.shortlink.admin.remote.dto.resp.*;
import com.yonagi.shortlink.admin.toolkit.EasyExcelWebUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接后台管理控制层
 * @date 2024/08/17 15:04
 */
@RestController
public class ShortLinkController {
    // TODO 后续重构为 Feign 调用
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};

    /**
     * 创建短链接
     * @param requestParam
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return shortLinkRemoteService.createShortLink(requestParam);
    }

    /**
     * 批量创建短链接
     * @param requestParam 批量创建短链接请求参数
     * @param response
     */
    @PostMapping("/api/short-link/admin/v1/create/batch")
    public void batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam, HttpServletResponse response) {
        Result<ShortLinkBatchCreateRespDTO> shortLinkBatchCreateRespDTOResult = shortLinkRemoteService.batchCreateShortLink(requestParam);
        if (shortLinkBatchCreateRespDTOResult.isSuccess()) {
            List<ShortLinkBaseInfoRespDTO> baseListInfos = shortLinkBatchCreateRespDTOResult.getData().getBaseListInfos();
            EasyExcelWebUtil.write(response, "批量创建短链接结果-b23短链接系统", ShortLinkBaseInfoRespDTO.class, baseListInfos);
        }
    }

    /**
     * 分页查询
     * @param requestParam 分页查询短链接请求参数
     * @return 分页查询结果
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return shortLinkRemoteService.pageShortLink(requestParam);
    }

    /**
     * 查询分组内短链接个数
     * @param requestParam 分组gids
     * @return gid和对应的短链接数
     */
    @GetMapping("/api/short-link/admin/v1/count")
    public Result<List<ShortLinkCountQueryRespDTO>> listGroupShortLinkCount(@RequestParam("requestParam")
                                                                                List<String> requestParam) {
        return shortLinkRemoteService.listGroupShortLinkCount(requestParam);
    }

    /**
     * 修改短链接
     * @param requestParam 短链接修改请求参数
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkRemoteService.updateShortLink(requestParam);
        return Results.success();
    }
}
