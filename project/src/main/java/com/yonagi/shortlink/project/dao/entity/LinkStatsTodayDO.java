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
 * @description 短链接今日统计实体
 * @date 2024/08/28 09:31
 */
@TableName("t_link_stats_today")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkStatsTodayDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * 短链接
     */
    private String fullShortUrl;

    /**
     * 日期
     */
    private Date date;

    /**
     * 今日pv
     */
    private Integer todayPv;

    /**
     * 今日uv
     */
    private Integer todayUv;

    /**
     * 今日ip数
     */
    private Integer todayUip;
}
