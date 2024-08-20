package com.yonagi.shortlink.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yonagi.shortlink.admin.common.convention.result.Result;
import com.yonagi.shortlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.yonagi.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 回收站服务
 * @date 2024/08/20 09:40
 */
public interface RecycleBinService {

    /**
     * 分页查询回收站
     * @param requestParam
     * @return
     */
    Result<IPage<ShortLinkPageRespDTO>> pageShortLinkForRecycleBin(ShortLinkRecycleBinPageReqDTO requestParam);
}
