package com.yonagi.shortlink.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 跳转链接白名单配置
 * @date 2024/08/30 13:22
 */
@Data
@Component
@ConfigurationProperties(prefix = "short-link.goto-domain.white-list")
public class GotoDomainWhiteListConfiguration {

    /**
     * 是否开启跳转链接白名单功能
     */
    private Boolean enable;

    /**
     * 可供跳转的原始网站名称集合
     */
    private String names;

    /**
     * 可供跳转的原始网站
     */
    private List<String> details;
}
