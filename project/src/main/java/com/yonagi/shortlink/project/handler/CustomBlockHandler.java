package com.yonagi.shortlink.project.handler;

import com.yonagi.shortlink.project.common.convention.result.Result;
import com.yonagi.shortlink.project.dto.req.ShortLinkBatchCreateReqDTO;
import com.yonagi.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkBatchCreateRespDTO;
import com.yonagi.shortlink.project.dto.resp.ShortLinkCreateRespDTO;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 降级策略
 * @date 2024/08/31 18:06
 */
public class CustomBlockHandler {

    public static Result<ShortLinkCreateRespDTO> createShortLinkBlockHandler(ShortLinkCreateReqDTO requestParam) {
        return new Result<ShortLinkCreateRespDTO>().setCode("B100000").setMessage("当前访问人数过多，请稍后再试");
    }

    public  static Result<ShortLinkBatchCreateRespDTO> createBatchShortLinkBlockHandler(ShortLinkBatchCreateReqDTO requestParam) {
        return new Result<ShortLinkBatchCreateRespDTO>().setCode("B100000").setMessage("当前访问人数过多，请稍后再试");
    }
}
