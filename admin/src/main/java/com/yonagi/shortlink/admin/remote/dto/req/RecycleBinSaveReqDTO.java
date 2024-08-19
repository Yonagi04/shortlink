package com.yonagi.shortlink.admin.remote.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 回收站保存请求参数
 * @date 2024/08/19 20:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecycleBinSaveReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 完整短链接
     */
    private String fullShortUrl;
}
