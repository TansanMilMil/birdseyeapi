package com.birdseyeapi.birdseyeapi;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("news")
public class NewsController {
    
    @GetMapping("/today-news")
    public List<News> getTodayNews() {
        List<News> newsList = Arrays.asList(
            new News(1, "dammy", null, "hoge")
        );
        return newsList;
    }
}
