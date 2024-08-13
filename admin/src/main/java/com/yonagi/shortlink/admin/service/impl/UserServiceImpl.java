package com.yonagi.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yonagi.shortlink.admin.common.convention.exception.ClientException;
import com.yonagi.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.yonagi.shortlink.admin.dao.entity.UserDO;
import com.yonagi.shortlink.admin.dao.mapper.UserMapper;
import com.yonagi.shortlink.admin.dto.resp.UserRespDTO;
import com.yonagi.shortlink.admin.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 用户服务层实现
 * @date 2024/08/13 10:56
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    /**
     * 根据用户名查询用户
     * @param userName 用户名
     * @return 用户实体UserRespDTO
     */
    @Override
    public UserRespDTO getUserByUserName(String userName) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, userName);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        UserRespDTO result = new UserRespDTO();
        if (userDO == null) {
            throw new ClientException(UserErrorCodeEnum.USER_NOT_EXIST);
        } else {
            BeanUtils.copyProperties(userDO, result);
            return result;
        }
    }
}
