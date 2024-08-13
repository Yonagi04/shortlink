package com.yonagi.shortlink.admin.controller;

import com.yonagi.shortlink.admin.common.convention.result.Result;
import com.yonagi.shortlink.admin.common.convention.result.Results;
import com.yonagi.shortlink.admin.dto.resp.UserRespDTO;
import com.yonagi.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description 用户控制层
 * @date 2024/08/13 10:58
 */
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/api/shortlink/v1/user/{username}")
    public Result<UserRespDTO> getUserByUserName(@PathVariable("username") String userName) {
        UserRespDTO result = userService.getUserByUserName(userName);
        return Results.success(result);
    }
}
