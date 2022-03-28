package com.birdseyeapi.birdseyeapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.rometools.rome.io.FeedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("news")
public class NewsController {

    @Autowired
    private NewsService newsService;
    
    @GetMapping("/today-news")
    public List<News> getTodayNews() throws IOException {
        List<News> newsList = newsService.getTodayNews();
        return newsList;
    }

    @GetMapping("/trends")
    public List<News> getTrends() throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
        List<News> newsList = newsService.getTrends();
        return newsList;
    }
}
