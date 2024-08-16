package com.yonagi.shortlink.project.controller;

import com.yonagi.shortlink.project.common.convention.result.Result;
import com.yonagi.shortlink.project.common.convention.result.Results;
import com.yonagi.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.yonagi.shortlink.project.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
     * 创建短链接
     * @return
     */
    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return Results.success(shortLinkService.createShortLink(requestParam));
    }
}
