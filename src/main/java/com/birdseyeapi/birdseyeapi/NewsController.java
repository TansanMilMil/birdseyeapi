package com.birdseyeapi.birdseyeapi;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public Map<String, List<News>> getTodayNews() throws IOException {
        Map<String, List<News>> newsList = newsService.getTodayNews();
        return newsList;
    }
}
