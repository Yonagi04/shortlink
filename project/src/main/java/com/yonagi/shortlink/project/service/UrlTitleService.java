package com.yonagi.shortlink.project.service;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description
 * @date 2024/08/19 13:56
 */
public interface UrlTitleService {

    /**
     * 根据URL获取标题
     * @param url
     * @return
     */
    String getTitleByUrl(String url);
}
