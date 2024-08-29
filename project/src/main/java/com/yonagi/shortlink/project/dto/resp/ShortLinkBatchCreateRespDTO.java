package com.yonagi.shortlink.project.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 批量创建短链接响应参数
 * @date 2024/08/29 10:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkBatchCreateRespDTO {

    /**
     * 成功数量
     */
    private Integer total;

    /**
     * 详细信息
     */
    private List<ShortLinkBaseInfoRespDTO> baseListInfos;
}
