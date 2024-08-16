package com.yonagi.shortlink.admin.dto.req;

import lombok.Data;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接修改请求参数
 * @date 2024/08/15 21:29
 */
@Data
public class ShortLinkGroupUpdateReqDTO {

    /**
     * 分组表示
     */
    private String gid;


    /**
     * 分组名
     */
    private String name;
}
