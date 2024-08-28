package com.yonagi.shortlink.project.dto.req;

import lombok.Data;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接分组监控数据请求参数
 * @date 2024/08/28 19:54
 */
@Data
public class ShortLinkGroupStatsReqDTO {

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
}
