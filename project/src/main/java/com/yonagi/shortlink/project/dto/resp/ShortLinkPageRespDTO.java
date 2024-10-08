package com.yonagi.shortlink.project.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接分页查询返回参数
 * @date 2024/08/17 10:55
 */
@Data
public class ShortLinkPageRespDTO {

    /**
     * id
     */
    private Long id;

    /**
     * 域名
     */
    private String domain;

    /**
     * 短链接
     */
    private String shortUri;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 原始链接
     */
    private String originUrl;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 有效期类型 0：永久有效 1：自定义
     */
    private Integer validDateType;

    /**
     * 启用标识 0：启用 1：未启用
     */
    private Integer enableStatus;

    /**
     * 有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validDate;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 描述
     */
    private String describe;

    /**
     * 网站标识
     */
    private String favicon;

    /**
     * 历史uv
     */
    private Integer totalUv;

    /**
     * 历史pv
     */
    private Integer totalPv;

    /**
     * 历史uip
     */
    private Integer totalUip;

    /**
     * 今日uv
     */
    private Integer todayUv;

    /**
     * 今日pv
     */
    private Integer todayPv;

    /**
     * 今日uip
     */
    private Integer todayUip;
}
