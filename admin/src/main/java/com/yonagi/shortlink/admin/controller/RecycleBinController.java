package com.yonagi.shortlink.admin.controller;

import com.yonagi.shortlink.admin.common.convention.result.Result;
import com.yonagi.shortlink.admin.common.convention.result.Results;
import com.yonagi.shortlink.admin.remote.ShortLinkRemoteService;
import com.yonagi.shortlink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 回收站控制层
 * @date 2024/08/19 20:25
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {};

    /**
     * 保存回收站
     * @param requestParam 保存回收站请求参数
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam) {
        shortLinkRemoteService.saveRecycleBin(requestParam);
        return Results.success();
    }
}
