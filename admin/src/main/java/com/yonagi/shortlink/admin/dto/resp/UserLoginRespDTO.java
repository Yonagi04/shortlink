package com.yonagi.shortlink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 用户登录返回token
 * @date 2024/08/15 09:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRespDTO {

    /**
     * token
     */
    private String token;
}
