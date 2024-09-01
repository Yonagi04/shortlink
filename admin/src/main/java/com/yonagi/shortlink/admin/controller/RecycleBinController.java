package com.yonagi.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yonagi.shortlink.admin.common.convention.result.Result;
import com.yonagi.shortlink.admin.common.convention.result.Results;
import com.yonagi.shortlink.admin.remote.ShortLinkActualRemoteService;
import com.yonagi.shortlink.admin.remote.dto.req.RecycleBinDeleteReqDTO;
import com.yonagi.shortlink.admin.remote.dto.req.RecycleBinRecoverReqDTO;
import com.yonagi.shortlink.admin.remote.dto.req.RecycleBinSaveReqDTO;
import com.yonagi.shortlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.yonagi.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.yonagi.shortlink.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final RecycleBinService recycleBinService;
    private final ShortLinkActualRemoteService shortLinkActualRemoteService;

    /**
     * 保存回收站
     * @param requestParam 保存回收站请求参数
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam) {
        shortLinkActualRemoteService.saveRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 分页查询回收站
     * @param requestParam 分页查询请求参数
     * @return
     */
    @GetMapping("/api/short-link/admin/v1/recycle-bin/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam) {
        return recycleBinService.pageShortLinkForRecycleBin(requestParam);
    }

    /**
     * 恢复回收站
     * @param requestParam 恢复回收站请求参数
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam) {
        shortLinkActualRemoteService.recoverRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 删除回收站
     * @param requestParam 删除回收站请求参数
     * @return
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/remove")
    public Result<Void> deleteRecycleBin(@RequestBody RecycleBinDeleteReqDTO requestParam) {
        shortLinkActualRemoteService.deleteRecycleBin(requestParam);
        return Results.success();
    }
}
