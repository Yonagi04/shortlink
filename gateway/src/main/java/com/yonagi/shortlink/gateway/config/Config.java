package com.yonagi.shortlink.gateway.config;

import lombok.Data;

import java.util.List;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 过滤器配置
 * @date 2024/09/01 11:43
 */
@Data
public class Config {

    /**
     * 白名单路径
     */
    private List<String> whitePathList;
}
