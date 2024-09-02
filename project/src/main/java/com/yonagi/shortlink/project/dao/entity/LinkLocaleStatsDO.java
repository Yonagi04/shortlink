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
 * @description 地区访问统计实体
 * @date 2024/08/21 16:27
 */
@Data
@TableName("t_link_locale_stats")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkLocaleStatsDO extends BaseDO {

    /**
     * ID
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
    private Integer cnt;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * atcode
     */
    private String adcode;
}
