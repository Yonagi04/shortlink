package com.yonagi.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yonagi.shortlink.project.dao.entity.LinkOSStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接统计操作系统持久层
 * @date 2024/08/24 08:07
 */
public interface LinkOSStatsMapper extends BaseMapper<LinkOSStatsDO> {
    
    /**
     * 记录基础访问监控数据
     * @param linkOSStatsDO
     */
    @Insert("INSERT INTO t_link_os_stats (full_short_url, gid, date, cnt, os, create_time, update_time, del_flag)" +
            "VALUES(#{linkOSStats.fullShortUrl}, #{linkOSStats.gid}, #{linkOSStats.date}, #{linkOSStats.cnt}, #{linkOSStats.os}, NOW(), NOW(), 0) ON DUPLICATE KEY UPDATE " +
            "cnt = cnt + #{linkOSStats.cnt};")
    void shortlinkOsStats(@Param("linkOSStats") LinkOSStatsDO linkOSStatsDO);
}
