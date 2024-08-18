package com.yonagi.shortlink.project.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接创建请求参数
 * @date 2024/08/16 12:45
 */

@Data
public class ShortLinkCreateReqDTO {

    /**
     * 域名
     */
    private String domain;

    /**
     * 原始链接
     */
    private String originUrl;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 创建类型 0：接口创建 1：控制台创建
     */
    private Integer createType;

    /**
     * 有效期类型 0：永久有效 1：自定义
     */
    private Integer validDateType;

    /**
     * 有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;

    /**
     * 描述
     */
    private String describe;
}
