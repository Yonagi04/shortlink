package com.yonagi.shortlink.project.service.impl;

import com.yonagi.shortlink.project.service.UrlTitleService;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Yonagi
 * @version 1.0
 * @program shortlink
 * @description
 * @date 2024/08/19 13:57
 */
@Service
public class UrlTitleServiceImpl implements UrlTitleService {

    @SneakyThrows
    @Override
    public String getTitleByUrl(String url) {
        URL targerUrl = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) targerUrl.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.connect();

        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Document document = Jsoup.connect(url).get();
            return document.title();
        }
        return "Error while fetching title.";
    }
}
