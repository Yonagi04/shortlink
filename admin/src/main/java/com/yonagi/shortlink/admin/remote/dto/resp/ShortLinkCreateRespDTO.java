package com.yonagi.shortlink.admin.remote.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接创建响应
 * @date 2024/08/16 12:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortLinkCreateRespDTO {

    /**
     * 分组信息
     */
    private String gid;

    /**
     * 域名
     */
    private String domain;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 原始链接
     */
    private String originUrl;
}
