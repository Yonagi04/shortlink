package com.yonagi.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yonagi.shortlink.project.dao.entity.ShortLinkDO;
import com.yonagi.shortlink.project.dto.req.RecycleBinSaveReqDTO;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 回收站服务层
 * @date 2024/08/19 20:37
 */
public interface RecycleBinService extends IService<ShortLinkDO> {
    void saveRecycleBin(RecycleBinSaveReqDTO requestParam);
}
