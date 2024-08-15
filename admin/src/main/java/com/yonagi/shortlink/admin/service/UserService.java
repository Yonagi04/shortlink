package com.yonagi.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yonagi.shortlink.admin.dao.entity.UserDO;
import com.yonagi.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.yonagi.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.yonagi.shortlink.admin.dto.resp.UserRespDTO;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 用户服务层接口
 * @date 2024/08/13 10:55
 */
public interface UserService extends IService<UserDO> {

    /**
     * 根据用户名查询用户
     * @param userName 用户名
     * @return 用户实体UserRespDTO
     */
    UserRespDTO getUserByUserName(String userName);

    /**
     * 根据用户名查询用户名是否存在
     * @param userName
     * @return true: 已经存在 false: 不存在
     */
    Boolean hasUserName(String userName);

    /**
     * 用户注册
     * @param requestParam 注册用户的请求参数
     */
    void register(UserRegisterReqDTO requestParam);

    /**
     * 用户更新信息
     * @param requestParam 更新信息的请求参数
     */
    void update(UserUpdateReqDTO requestParam);
}
