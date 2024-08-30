package com.yonagi.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yonagi.shortlink.project.dao.entity.ShortLinkDO;
import com.yonagi.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkBatchCreateReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkUpdateReqDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkBatchCreateRespDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkCountQueryRespDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkPageRespDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接服务层接口
 * @date 2024/08/15 15:26
 */
public interface ShortLinkService extends IService<ShortLinkDO> {

    /**
     * 短链接跳转
     * @param shortUri 短链接
     * @param request 请求
     * @param response 响应
     */
    void restoreUrl(String shortUri, HttpServletRequest request, HttpServletResponse response);

    /**
     * 短链接统计
     * @param fullShortUrl
     * @param gid
     * @param shortLinkStatsRecord
     */
    void shortLinkStats(String fullShortUrl, String gid, ShortLinkStatsRecordDTO shortLinkStatsRecord);

    /**
     * 创建短链接
     * @param requestParam
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);

    /**
     * 批量创建短链接
     * @param requestParam
     * @return
     */
    ShortLinkBatchCreateRespDTO batchCreateShortLink(ShortLinkBatchCreateReqDTO requestParam);

    /**
     * 短链接分页查询
     * @param requestParam 分页查询短链接请求参数
     * @return 分页查询结果
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam);

    /**
     * 查询短链接组内短链接个数
     * @param requestParam
     * @return
     */
    List<ShortLinkCountQueryRespDTO> listGroupShortLinkCount(List<String> requestParam);

    /**
     * 修改短链接
     * @param requestParam
     */
    void updateShortLink(ShortLinkUpdateReqDTO requestParam);
}
