package com.yonagi.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yonagi.shortlink.project.common.database.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 访问日志监控表实体
 * @date 2024/08/27 08:34
 */
@Data
@TableName("t_link_access_logs")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkAccessLogsDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 用户信息
     */
    private String user;

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
}
