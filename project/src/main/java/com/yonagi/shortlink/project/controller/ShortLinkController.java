package com.yonagi.shortlink.project.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yonagi.shortlink.project.common.convention.result.Result;
import com.yonagi.shortlink.project.common.convention.result.Results;
import com.yonagi.shortlink.project.dto.req.ShortLinkBatchCreateReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkBatchCreateRespDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkCountQueryRespDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import com.yonagi.shortlink.project.handler.CustomBlockHandler;
import com.yonagi.shortlink.project.service.ShortLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接控制层
 * @date 2024/08/15 15:37
 */
@RestController
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkService shortLinkService;

    /**
     * 短链接跳转
     * @param shortUri 短链接
     * @param request 请求
     * @param response 响应
     * @return
     */
    @GetMapping("/{short-uri}")
    public void restoreUrl(@PathVariable("short-uri") String shortUri,
                                           HttpServletRequest request, HttpServletResponse response) {
        shortLinkService.restoreUrl(shortUri, request, response);
    }

    /**
     * 创建短链接
     * @param requestParam 创建短链接请求参数
     * @return
     */
    @SentinelResource(
            value = "create_short-link",
            blockHandler = "createShortLinkBlockHandler",
            blockHandlerClass = CustomBlockHandler.class
    )
    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return Results.success(shortLinkService.createShortLink(requestParam));
    }

    /**
     * 批量创建短链接
     * @param requestParam 批量短链接请求参数
     * @return
     */
    @PostMapping("/api/short-link/v1/create/batch")
    @SentinelResource(
            value = "create_short-link",
            blockHandler = "createBatchShortLinkBlockHandler",
            blockHandlerClass = CustomBlockHandler.class
    )
    public Result<ShortLinkBatchCreateRespDTO> batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam) {
        return Results.success(shortLinkService.batchCreateShortLink(requestParam));
    }

    /**
     * 分页查询
     * @param requestParam 分页查询短链接请求参数
     * @return 分页查询结果
     */
    @GetMapping("/api/short-link/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return Results.success(shortLinkService.pageShortLink(requestParam));
    }

    /**
     * 查询分组内短链接个数
     * @param requestParam 分组gids
     * @return gid和对应的短链接数
     */
    @GetMapping("/api/short-link/v1/count")
    public Result<List<ShortLinkCountQueryRespDTO>> listGroupShortLinkCount(@RequestParam("requestParam")
                                                                                List<String> requestParam) {
        return Results.success(shortLinkService.listGroupShortLinkCount(requestParam));
    }

    /**
     * 修改短链接
     * @param requestParam 短链接修改请求参数
     * @return
     */
    @PostMapping("/api/short-link/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkService.updateShortLink(requestParam);
        return Results.success();
    }
}
