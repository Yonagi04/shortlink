package com.yonagi.shortlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接分页查询请求参数
 * @date 2024/08/17 10:54
 */
@Data
public class ShortLinkPageReqDTO extends Page {

    /**
     * 分组id
     */
    private String gid;
}
