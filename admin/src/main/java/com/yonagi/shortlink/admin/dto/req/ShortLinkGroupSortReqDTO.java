package com.yonagi.shortlink.admin.dto.req;

import lombok.Data;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接分组排序请求参数
 * @date 2024/08/16 09:56
 */
@Data
public class ShortLinkGroupSortReqDTO {

    /**
     * 分组id
     */
    private String gid;

    /**
     * 排序字段
     */
    private Integer sortOrder;
}