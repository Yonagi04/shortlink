package com.yonagi.shortlink.admin.dto.req;

import lombok.Data;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接保存请求参数
 * @date 2024/08/15 16:19
 */
@Data
public class ShortLinkGroupSaveDTO {

    /**
     * 分组名称
     */
    private String name;
}
