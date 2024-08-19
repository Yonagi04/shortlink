package com.yonagi.shortlink.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description
 * @date 2024/08/19 13:42
 */
@Controller
public class ShortLinkNotfoundController {

    @RequestMapping("/page/404")
    public String notfound() {
        return "404";
    }
}
