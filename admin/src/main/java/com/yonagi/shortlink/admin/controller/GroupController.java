package com.yonagi.shortlink.admin.controller;

import com.yonagi.shortlink.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

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
}
