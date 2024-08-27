package com.yonagi.shortlink.project.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接访问记录监控响应
 * @date 2024/08/27 10:48
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkStatsAccessRecordRespDTO {

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * ip
     */
    private String ip;

    /**
     * 设备
     */
    private String device;

    /**
     * 网络
     */
    private String network;

    /**
     * 地区
     */
    private String locale;

    /**
     * 访客类型
     */
    private String uvType;

    /**
     * 用户
     */
    private String user;

    /**
     * 访问时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
