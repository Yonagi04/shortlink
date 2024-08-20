package com.yonagi.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yonagi.shortlink.project.dao.entity.ShortLinkDO;
import com.yonagi.shortlink.project.dto.req.RecycleBinRecoverReqDTO;
import com.yonagi.shortlink.project.dto.req.RecycleBinSaveReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkPageRespDTO;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 回收站服务层
 * @date 2024/08/19 20:37
 */
public interface RecycleBinService extends IService<ShortLinkDO> {

    /**
     * 保存回收站
     * @param requestParam 保存回收站请求参数
     */
    void saveRecycleBin(RecycleBinSaveReqDTO requestParam);

    /**
     * 分页查询回收站
     * @param requestParam 分页查询请求参数
     * @return 分页查询响应
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam);

    /**
     * 恢复回收站
     * @param requestParam
     */
    void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam);
}
