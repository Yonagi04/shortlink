package com.yonagi.shortlink.admin.controller;

import com.yonagi.shortlink.admin.common.convention.result.Result;
import com.yonagi.shortlink.admin.common.convention.result.Results;
import com.yonagi.shortlink.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.yonagi.shortlink.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.yonagi.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.yonagi.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 短链接分组控制层
 * @date 2024/08/15 15:47
 */
@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    /**
     * 新增短链接分组
     * @param requestParam 新增短链接分组请求参数
     * @return
     */
    @PostMapping("/api/short-link/v1/group")
    public Result<Void> save(@RequestBody ShortLinkGroupSaveReqDTO requestParam) {
        groupService.saveGroup(requestParam.getName());
        return Results.success();
    }

    /**
     * 查询短链接分组
     * @return 短链接分组
     */
    @GetMapping("/api/short-link/v1/group")
    public Result<List<ShortLinkGroupRespDTO>> listGroup() {
        return Results.success(groupService.listGroup());
    }

    /**
     * 更新短链接分组
     * @param requestParam 修改短链接分组请求参数
     * @return
     */
    @PutMapping("/api/short-link/v1/group")
    public Result<Void> update(@RequestBody ShortLinkGroupUpdateReqDTO requestParam) {
        groupService.updateGroup(requestParam);
        return Results.success();
    }

    /**
     * 删除短链接分组
     * @param gid 短链接分组id
     * @return
     */
    @DeleteMapping("/api/short-link/v1/group")
    public Result<Void> delete(@RequestParam String gid) {
        groupService.deleteGroup(gid);
        return Results.success();
    }
}
