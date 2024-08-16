package com.yonagi.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yonagi.shortlink.admin.common.biz.user.UserContext;
import com.yonagi.shortlink.admin.dao.entity.GroupDO;
import com.yonagi.shortlink.admin.dao.mapper.GroupMapper;
import com.yonagi.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.yonagi.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.yonagi.shortlink.admin.service.GroupService;
import com.yonagi.shortlink.admin.toolkit.RandomGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接分组服务层实现
 * @date 2024/08/15 15:46
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    @Override
    public void saveGroup(String groupName) {
        String gid;
        while (true) {
            gid = RandomGenerator.generateRandomString();
            if (hasGid(gid)) {
                break;
            }
        }
        GroupDO groupDO = GroupDO.builder()
                .name(groupName)
                .gid(gid)
                .sortOrder(0)
                .username(UserContext.getUsername())
                .build();
        baseMapper.insert(groupDO);
    }

    /**
     * 查询Group id是否存在
     * @param gid Group id
     * @return
     */
    private Boolean hasGid(String gid) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUsername, UserContext.getUsername());
        GroupDO groupDO = baseMapper.selectOne(queryWrapper);
        return groupDO == null;
    }

    @Override
    public List<ShortLinkGroupRespDTO> listGroup() {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime);
        List<GroupDO> groupDOList = baseMapper.selectList(queryWrapper);
        return BeanUtil.copyToList(groupDOList, ShortLinkGroupRespDTO.class);
    }

    @Override
    public void updateGroup(ShortLinkGroupUpdateReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getGid, requestParam.getGid())
                .eq(GroupDO::getDelFlag, 0);
        GroupDO groupDO = new GroupDO();
        groupDO.setName(requestParam.getName());
        baseMapper.update(groupDO, queryWrapper);
    }
}
