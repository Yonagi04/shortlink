package com.yonagi.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yonagi.shortlink.admin.common.convention.result.Result;
import com.yonagi.shortlink.admin.remote.dto.ShortLinkRemoteService;
import com.yonagi.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.yonagi.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.yonagi.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.yonagi.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
     * @return
     */
    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        return shortLinkRemoteService.createShortLink(requestParam);
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
}
