package com.yonagi.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yonagi.shortlink.admin.dao.entity.GroupDO;
import com.yonagi.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.yonagi.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;

import java.util.List;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接分组服务层接口
 * @date 2024/08/15 15:45
 */
public interface GroupService extends IService<GroupDO> {

    /**
     * 新增短链接分组
     * @param groupName 分组名称
     */
    void saveGroup(String groupName);

    /**
     * 查询短链接分组
     * @return
     */
    List<ShortLinkGroupRespDTO> listGroup();

    /**
     * 更新短链接分组
     * @param requestParam 修改分组请求参数
     */
    void updateGroup(ShortLinkGroupUpdateReqDTO requestParam);
}
