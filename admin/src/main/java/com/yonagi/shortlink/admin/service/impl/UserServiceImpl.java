package com.yonagi.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yonagi.shortlink.admin.common.constant.RedisCacheConstant;
import com.yonagi.shortlink.admin.common.convention.exception.ClientException;
import com.yonagi.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.yonagi.shortlink.admin.dao.entity.UserDO;
import com.yonagi.shortlink.admin.dao.mapper.UserMapper;
import com.yonagi.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.yonagi.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.yonagi.shortlink.admin.dto.resp.UserRespDTO;
import com.yonagi.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;

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

    /**
     * 查询用户名是否存在
     * @param userName
     * @return true: 已经存在 false: 不存在
     */
    @Override
    public Boolean hasUserName(String userName) {
        return userRegisterCachePenetrationBloomFilter.contains(userName);
    }

    /**
     * 用户注册
     * @param requestParam 注册用户的请求参数
     */
    @Override
    public void register(UserRegisterReqDTO requestParam) {
        if (hasUserName(requestParam.getUsername())) {
            throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
        }
        RLock lock = redissonClient.getLock(RedisCacheConstant.LOCK_USER_REGISTER_KEY + requestParam.getUsername());
        try {
            if (lock.tryLock()) {
                int insert = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
                if (insert < 1) {
                    throw new ClientException(UserErrorCodeEnum.USER_SAVE_ERROR);
                }
                userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
            } else {
                throw new ClientException(UserErrorCodeEnum.USER_NAME_EXIST);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(UserUpdateReqDTO requestParam) {
        // TODO 验证当前要修改的用户是否为登录用户
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        int update = baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
    }
}
