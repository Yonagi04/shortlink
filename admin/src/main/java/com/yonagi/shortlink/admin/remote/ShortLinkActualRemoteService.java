package com.yonagi.shortlink.admin.remote;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yonagi.shortlink.admin.common.convention.result.Result;
import com.yonagi.shortlink.admin.remote.dto.req.*;
import com.yonagi.shortlink.admin.remote.dto.resp.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description Feign远程调用
 * @date 2024/09/01 10:05
 */
@FeignClient("short-link-project")
public interface ShortLinkActualRemoteService {

    /**
     * 创建短链接
     * @param requestParam 短链接创建请求
     * @return 短链接创建响应
     */
    @PostMapping("/api/short-link/v1/create")
    Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam);

    /**
     * 批量创建短链接
     * @param requestParam 批量创建短链接请求参数
     * @return 批量创建短链接响应
     */
    @PostMapping("/api/short-link/v1/create/batch")
    Result<ShortLinkBatchCreateRespDTO> batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam);

    /**
     * 分页查询短链接
     * @param requestParam 分页查询请求参数
     * @return 分页查询结果
     */
    @GetMapping("/api/short-link/v1/page")
    Result<Page<ShortLinkPageRespDTO>> pageShortLink(@SpringQueryMap ShortLinkPageReqDTO requestParam);

    /**
     * 查询分组内短链接个数
     * @param requestParam 分组gids
     * @return gid和对应的短链接数
     */
    @GetMapping("/api/short-link/v1/count")
    Result<List<ShortLinkCountQueryRespDTO>> listGroupShortLinkCount(@RequestParam("requestParam") List<String> requestParam);

    /**
     * 修改短链接
     * @param requestParam 短链接修改请求参数
     */
    @PostMapping("/api/short-link/v1/update")
    void updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam);

    /**
     * 通过URL获取原链接标题
     * @param url
     * @return
     */
    @GetMapping("/api/short-link/v1/title")
    Result<String> getTitleByUrl(@RequestParam("url") String url);

    /**
     * 保存回收站
     * @param requestParam 保存回收站请求参数
     * @return
     */
    @PostMapping("/api/short-link/v1/recycle-bin/save")
    void saveRecycleBin(@RequestBody RecycleBinSaveReqDTO requestParam);

    /**
     * 分页查询回收站
     * @param requestParam 分页查询请求参数
     * @return
     */
    @GetMapping("/api/short-link/v1/recycle-bin/page")
    Result<IPage<ShortLinkPageRespDTO>> pageShortLinkForRecycleBin(@SpringQueryMap ShortLinkRecycleBinPageReqDTO requestParam);

    /**
     * 恢复回收站
     * @param requestParam 恢复回收站请求参数
     */
    @PostMapping("/api/short-link/v1/recycle-bin/recover")
    void recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam);

    /**
     * 删除回收站
     * @param requestParam 删除回收站请求参数
     */
    @PostMapping("/api/short-link/v1/recycle-bin/remove")
    void deleteRecycleBin(@RequestBody RecycleBinDeleteReqDTO requestParam);

    /**
     * 访问单个短链接指定时间内的监控数据
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/v1/stats")
    ShortLinkStatsRespDTO oneShortLinkStats(@SpringQueryMap ShortLinkStatsReqDTO requestParam);

    /**
     * 访问单个短链接指定时间内的监控数据
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/v1/stats/group")
    ShortLinkStatsRespDTO groupShortLinkStats(@SpringQueryMap ShortLinkGroupStatsReqDTO requestParam);

    /**
     * 访问单个短链接指定时间内访问记录的监控数据
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/v1/stats/access-record")
    IPage<ShortLinkStatsAccessRecordRespDTO> oneShortLinkStatsAccessRecord(@SpringQueryMap ShortLinkStatsAccessRecordReqDTO requestParam);

    /**
     * 访问短链接组指定时间内访问记录的监控数据
     * @param requestParam
     * @return
     */
    @GetMapping("/api/short-link/v1/stats/access-record")
    Page<ShortLinkStatsAccessRecordRespDTO> groupShortLinkStatsAccessRecord(@SpringQueryMap ShortLinkGroupStatsAccessRecordReqDTO requestParam);
}
