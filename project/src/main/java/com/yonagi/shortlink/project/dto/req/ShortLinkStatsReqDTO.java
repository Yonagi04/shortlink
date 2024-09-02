package com.yonagi.shortlink.project.dto.req;

import lombok.Data;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接监控请求参数
 * @date 2024/08/27 10:46
 */
@Data
public class ShortLinkStatsReqDTO {

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;

    /**
     * 启用标识 0：启用 1：未启用
     */
    private Integer enableStatus;
}
