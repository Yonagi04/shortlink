package com.yonagi.shortlink.admin.common.biz.user;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 用户信息实体
 * @date 2024/08/15 20:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDTO {

    /**
     * 用户 ID
     */
    @JSONField(name = "id")
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;
}
