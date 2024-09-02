package com.yonagi.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yonagi.shortlink.project.dao.entity.LinkAccessLogsDO;
import com.yonagi.shortlink.project.dao.entity.LinkAccessStatsDO;
import com.yonagi.shortlink.project.dto.req.ShortLinkGroupStatsAccessRecordReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkGroupStatsReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkStatsReqDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 访问日志监控表持久层
 * @date 2024/08/27 08:36
 */
public interface LinkAccessLogsMapper extends BaseMapper<LinkAccessLogsDO> {

    /**
     * 根据短链接获取指定日期内高频访问IP数据
     */
    @Select("SELECT " +
            "   tlal.ip, " +
            "   COUNT(tlal.ip) AS count " +
            "FROM " +
            "   t_link tl INNER JOIN " +
            "   t_link_access_logs tlal ON tl.full_short_url = tlal.full_short_url " +
            "WHERE " +
            "   tlal.full_short_url = #{param.fullShortUrl} " +
            "   AND tl.gid = #{param.gid} " +
            "   AND tl.del_flag = '0' " +
            "   AND tl.enable_status = #{param.enableStatus}" +
            "   AND tlal.create_time BETWEEN #{param.startDate} AND #{param.endDate} " +
            "GROUP BY " +
            "   tlal.full_short_url, tl.gid, tlal.ip " +
            "ORDER BY " +
            "   count DESC " +
            "LIMIT 5;")
    List<HashMap<String, Object>> listTopIpByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);

    /**
     * 根据短链接组获取指定日期内高频访问IP数据
     */
    @Select("SELECT " +
            "   tlal.ip, " +
            "   COUNT(tlal.ip) AS count " +
            "FROM " +
            "   t_link tl INNER JOIN " +
            "   t_link_access_logs tlal ON tl.full_short_url = tlal.full_short_url " +
            "WHERE " +
            "   tl.gid = #{param.gid} " +
            "   AND tl.del_flag = '0' " +
            "   AND tl.enable_status = '0' " +
            "   AND tlal.create_time BETWEEN #{param.startDate} AND #{param.endDate} " +
            "GROUP BY " +
            "   tl.gid, tlal.ip " +
            "ORDER BY" +
            "   count DESC " +
            "LIMIT 5;")
    List<HashMap<String, Object>> listTopIpByGroup(@Param("param") ShortLinkGroupStatsReqDTO requestParam);

    /**
     * 根据短链接获取指定日期内新旧访客数据
     */
    @Select("SELECT " +
            "    SUM(old_user) AS oldUserCnt, " +
            "    SUM(new_user) AS newUserCnt " +
            "FROM ( " +
            "    SELECT " +
            "        CASE WHEN COUNT(DISTINCT DATE(tlal.create_time)) > 1 THEN 1 ELSE 0 END AS old_user, " +
            "        CASE WHEN COUNT(DISTINCT DATE(tlal.create_time)) = 1 AND MAX(tlal.create_time) >= #{param.startDate} AND MAX(tlal.create_time) <= #{param.endDate} THEN 1 ELSE 0 END AS new_user " +
            "    FROM " +
            "        t_link tl INNER JOIN " +
            "        t_link_access_logs tlal ON tl.full_short_url = tlal.full_short_url " +
            "    WHERE " +
            "        tlal.full_short_url = #{param.fullShortUrl} " +
            "        AND tl.gid = #{param.gid} " +
            "        AND tl.enable_status = #{param.enableStatus} " +
            "        AND tl.del_flag = '0' " +
            "    GROUP BY " +
            "       tlal.user " +
            ") AS user_counts;")
    HashMap<String, Object> findUvTypeCntByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);


    /**
     * 获取用户信息是否新老访客
     * @param gid
     * @param fullShortUrl
     * @param enableStatus
     * @param startDate
     * @param endDate
     * @param userAccessLogList
     * @return
     */
    @Select("<script> " +
            "SELECT " +
            "    tlal.user, " +
            "    CASE " +
            "        WHEN MIN(tlal.create_time) BETWEEN #{startDate} AND #{endDate} THEN '新访客' " +
            "        ELSE '老访客' " +
            "    END AS uvType " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_link_access_logs tlal ON tl.full_short_url = tlal.full_short_url " +
            "WHERE " +
            "    tlal.full_short_url = #{fullShortUrl} " +
            "    AND tl.gid = #{gid} " +
            "    AND tl.del_flag = '0' " +
            "    AND tl.enable_status = #{enableStatus} " +
            "    AND tlal.user IN " +
            "    <foreach item='item' index='index' collection='userAccessLogsList' open='(' separator=',' close=')'> " +
            "        #{item} " +
            "    </foreach> " +
            "GROUP BY " +
            "    tlal.user;" +
            "</script>")
    List<Map<String, Object>> selectUvTypeByUser(@Param("gid") String gid,
                                                 @Param("fullShortUrl") String fullShortUrl,
                                                 @Param("enableStatus") Integer enableStatus,
                                                 @Param("startDate") String startDate,
                                                 @Param("endDate") String endDate,
                                                 @Param("userAccessLogList") List<String> userAccessLogList);

    /**
     * 判断分组用户信息是否是老访客
     * @param fullShortUrl
     * @param gid
     * @param startDate
     * @param endDate
     * @param userAccessLogList
     * @return
     */
    @Select("<script> " +
            "SELECT " +
            "    tlal.user, " +
            "    CASE " +
            "        WHEN MIN(tlal.create_time) BETWEEN #{startDate} AND #{endDate} THEN '新访客' " +
            "        ELSE '老访客' " +
            "    END AS uvType " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_link_access_logs tlal ON tl.full_short_url = tlal.full_short_url " +
            "WHERE " +
            "    tl.gid = #{gid} " +
            "    AND tl.del_flag = '0' " +
            "    AND tl.enable_status = '0' " +
            "    AND tlal.user IN " +
            "    <foreach item='item' index='index' collection='userAccessLogsList' open='(' separator=',' close=')'> " +
            "        #{item} " +
            "    </foreach> " +
            "GROUP BY " +
            "    tlal.user;" +
            "</script>")
    List<Map<String, Object>> selectGroupUvTypeByUser(@Param("gid") String gid,
                                                      @Param("startDate") String startDate,
                                                      @Param("endDate") String endDate,
                                                      @Param("userAccessLogList") List<String> userAccessLogsList);

    /**
     * 根据短链接获取指定日期内pv,uv,uip数据
     * @param requestParam
     * @return
     */
    @Select("SELECT " +
            "   COUNT(tlal.user) AS pv, " +
            "   COUNT(DISTINCT tlal.user) AS uv, " +
            "   COUNT(DISTINCT tlal.ip) AS uip " +
            "FROM " +
            "   t_link tl INNER JOIN " +
            "   t_link_access_logs tlal ON tl.full_short_url = tlal.full_short_url " +
            "WHERE " +
            "   tlal.full_short_url = #{param.fullShortUrl} " +
            "   AND tl.gid = #{param.gid} " +
            "   AND tl.del_flag = '0' " +
            "   AND tl.enable_status = #{param.enableStatus} " +
            "   AND tlal.create_time BETWEEN #{param.startDate} AND #{param.endDate} " +
            "GROUP BY " +
            "   tlal.full_short_url, tl.gid;")
    LinkAccessStatsDO findUvPvUipStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);

    /**
     * 根据短链接组获取指定日期内pv,uv,uip数据
     * @param requestParam
     * @return
     */
    @Select("SELECT " +
            "    COUNT(tlal.user) AS pv, " +
            "    COUNT(DISTINCT tlal.user) AS uv, " +
            "    COUNT(DISTINCT tlal.ip) AS uip " +
            "FROM " +
            "    t_link tl INNER JOIN " +
            "    t_link_access_logs tlal ON tl.full_short_url = tlal.full_short_url " +
            "WHERE " +
            "    tl.gid = #{param.gid} " +
            "    AND tl.del_flag = '0' " +
            "    AND tl.enable_status = '0' " +
            "    AND tlal.create_time BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    tl.gid;")
    LinkAccessStatsDO findUvPvUipStatsByGroup(@Param("param") ShortLinkGroupStatsReqDTO requestParam);

    /**
     * 短链接组分页
     * @param requestParam
     * @return
     */
    @Select("SELECT " +
            "    tlal.* " +
            "FROM " +
            "    t_link_1 tl " +
            "    INNER JOIN t_link_access_logs tlal ON tl.full_short_url = tlal.full_short_url " +
            "WHERE " +
            "    tl.gid = #{param.gid} " +
            "    AND tl.del_flag = '0' " +
            "    AND tl.enable_status = '0' " +
            "    AND tlal.create_time BETWEEN #{param.startDate} and #{param.endDate} " +
            "ORDER BY " +
            "    tlal.create_time DESC")
    IPage<LinkAccessLogsDO> selectGroupPage(@Param("param") ShortLinkGroupStatsAccessRecordReqDTO requestParam);
}
