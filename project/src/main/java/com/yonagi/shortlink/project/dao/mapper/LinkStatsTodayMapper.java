package com.yonagi.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yonagi.shortlink.project.dao.entity.LinkStatsTodayDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接今日访问统计持久层
 * @date 2024/08/28 09:33
 */
public interface LinkStatsTodayMapper extends BaseMapper<LinkStatsTodayDO> {

    /**
     * 记录今日监控数据
     * @param linkStatsTodayDO
     */
    @Insert("INSERT INTO t_link_stats_today (full_short_url, gid, date, today_pv, today_uv, today_uip, create_time, update_time, del_flag)" +
            "VALUES(#{linkTodayStats.fullShortUrl}, #{linkTodayStats.gid}, #{linkTodayStats.date}, #{linkTodayStats.todayPv}, #{linkTodayStats.todayUv}, #{linkTodayStats.todayUip}, NOW(), NOW(), 0) ON DUPLICATE KEY UPDATE " +
            "today_uv = today_uv + #{linkTodayStats.todayUv}," +
            "today_uv = today_pv + #{linkTodayStats.todayPv}," +
            "today_uip = today_uip + #{linkTodayStats.todayUip}")
    void shortlinkTodayStats(@Param("linkTodayStats") LinkStatsTodayDO linkStatsTodayDO);
}
