package com.yonagi.shortlink.project.dto.resp;

import lombok.Data;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接组统计数量返回参数
 * @date 2024/08/17 18:08
 */
@Data
public class ShortLinkCountQueryRespDTO {

    /**
     * 分组id
     */
    private String gid;

    /**
     * 短链接数
     */
    private Integer shortLinkCount;
}
