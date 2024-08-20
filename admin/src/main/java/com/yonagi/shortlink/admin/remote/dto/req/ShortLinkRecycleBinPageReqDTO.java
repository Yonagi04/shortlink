package com.yonagi.shortlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.List;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接回收站分页查询请求参数
 * @date 2024/08/17 10:54
 */
@Data
public class ShortLinkRecycleBinPageReqDTO extends Page {

    /**
     * 分组id
     */
    private List<String> gidList;
}
