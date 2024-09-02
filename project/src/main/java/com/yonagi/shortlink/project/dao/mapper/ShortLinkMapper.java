package com.yonagi.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yonagi.shortlink.project.dao.entity.ShortLinkDO;
import com.yonagi.shortlink.project.dto.req.ShortLinkPageReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.apache.ibatis.annotations.Param;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接持久层
 * @date 2024/08/15 15:25
 */
public interface ShortLinkMapper extends BaseMapper<ShortLinkDO> {

    /**
     * 短链接访问统计自增
     */
    void incrementStats(
            @Param("gid") String gid,
            @Param("fullShortUrl") String fullShortUrl,
            @Param("totalPv") Integer totalPv,
            @Param("totalUv") Integer totalUv,
            @Param("totalUip") Integer totalUip
    );

    /**
     * 分页统计短链接
     * @param shortLinkPageReqDTO
     * @return
     */
    IPage<ShortLinkDO> pageLink(ShortLinkPageReqDTO shortLinkPageReqDTO);

    /**
     * 分页统计回收站短链接
     */
    IPage<ShortLinkDO> pageRecycleBinLink(ShortLinkRecycleBinPageReqDTO requestParam);
}
