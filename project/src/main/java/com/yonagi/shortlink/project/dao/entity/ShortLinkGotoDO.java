package com.yonagi.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接跳转实体
 * @date 2024/08/18 12:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_link_goto")
public class ShortLinkGotoDO {

    /**
     * id
     */
    private Long id;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 完整短链接
     */
    private String fullShortUrl;
}
