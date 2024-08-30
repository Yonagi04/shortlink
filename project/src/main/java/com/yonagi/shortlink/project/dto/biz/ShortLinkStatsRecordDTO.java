package com.yonagi.shortlink.project.dto.biz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接统计实体
 * @date 2024/08/30 10:18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkStatsRecordDTO {

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 访问用户IP
     */
    private String remoteAddr;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作设备
     */
    private String device;

    /**
     * 网络
     */
    private String network;

    /**
     * UV
     */
    private String uv;

    /**
     * UV访问标识
     */
    private Boolean uvFirstFlag;

    /**
     * UIP访问标识
     */
    private Boolean uipFirstFlag;
}
