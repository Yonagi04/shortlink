package com.yonagi.shortlink.project.controller;

import com.yonagi.shortlink.project.common.convention.result.Result;
import com.yonagi.shortlink.project.common.convention.result.Results;
import com.yonagi.shortlink.project.service.UrlTitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description
 * @date 2024/08/19 13:53
 */
@RestController
@RequiredArgsConstructor
public class UrlTitleController {

    private final UrlTitleService urlTitleService;

    /**
     * 根据URL获取网站标题
     * @param url
     * @return
     */
    @GetMapping("/api/short-link/v1/title")
    public Result<String> getTitleByUrl(@RequestParam("url") String url) {
        return Results.success(urlTitleService.getTitleByUrl(url));
    }
}
