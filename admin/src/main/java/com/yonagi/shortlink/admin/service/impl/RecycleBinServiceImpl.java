package com.yonagi.shortlink.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yonagi.shortlink.admin.common.biz.user.UserContext;
import com.yonagi.shortlink.admin.common.convention.exception.ServerException;
import com.yonagi.shortlink.admin.common.convention.result.Result;
import com.yonagi.shortlink.admin.dao.entity.GroupDO;
import com.yonagi.shortlink.admin.dao.mapper.GroupMapper;
import com.yonagi.shortlink.admin.remote.ShortLinkActualRemoteService;
import com.yonagi.shortlink.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import com.yonagi.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.yonagi.shortlink.admin.service.RecycleBinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 回收站服务实现
 * @date 2024/08/20 09:40
 */
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {

    private final ShortLinkActualRemoteService shortLinkActualRemoteService;
    private final GroupMapper groupMapper;

    @Override
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLinkForRecycleBin(ShortLinkRecycleBinPageReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);
        List<GroupDO> groupDOList = groupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(groupDOList)){
            throw new ServerException("用户无分组信息");
        }
        requestParam.setGidList(groupDOList.stream().map(GroupDO::getGid).toList());
        return shortLinkActualRemoteService.pageShortLinkForRecycleBin(requestParam);
    }
}
