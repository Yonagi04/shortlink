package com.yonagi.shortlink.admin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 用户风控配置类
 * @date 2024/08/31 17:14
 */
@Data
@Component
@ConfigurationProperties(prefix = "short-link.flow-limit")
public class UserFlowRiskControlConfiguration {

    /**
     * 是否开启风控
     */
    private Boolean enable;

    /**
     * 时间窗口，单位/s
     */
    private String timeWindow;

    /**
     * 时间窗口内最大访问次数
     */
    private Long maxAccessCount;
}
