package com.yonagi.shortlink.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 网关错误返回信息
 * @date 2024/09/01 11:44
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayErrorResult {

    /**
     * HTTP 状态码
     */
    private Integer status;

    /**
     * 返回信息
     */
    private String message;
}
