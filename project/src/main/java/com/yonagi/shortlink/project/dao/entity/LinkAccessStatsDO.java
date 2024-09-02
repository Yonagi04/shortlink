package com.yonagi.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yonagi.shortlink.project.common.database.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接访问统计持久层实体
 * @date 2024/08/21 10:54
 */
@Data
@TableName("t_link_access_stats")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkAccessStatsDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 日期
     */
    private Date date;

    /**
     * 访问量
     */
    private Integer pv;

    /**
     * 独立访客数
     */
    private Integer uv;

    /**
     * 独立ip数
     */
    private Integer uip;

    /**
     * 小时
     */
    private Integer hour;

    /**
     * 星期
     */
    private Integer weekday;
}
