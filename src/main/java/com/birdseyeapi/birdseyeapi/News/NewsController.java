package com.birdseyeapi.birdseyeapi.News;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.birdseyeapi.birdseyeapi.Trends.TrendsService;
import com.rometools.rome.io.FeedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("news")
public class NewsController {
    private final NewsService newsService;
    private final TrendsService trendsService;

    @GetMapping("/today-news")
    public List<NewsWithReactionCount> getTodayNews() throws IOException {
        List<NewsWithReactionCount> newsList = newsService.getTodayNews();
        return newsList;
    }

    @GetMapping("/trends")
    public List<News> getTrends() throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
        List<News> newsList = trendsService.getTrends();
        return newsList;
    }

    @GetMapping("/news-reactions")
    public List<NewsReaction> getNewsReactions(@RequestParam long id) {
        List<NewsReaction> reactions = newsService.getNewsReactions(id);
        return reactions;
    }

    @PostMapping("/scrape")
    public boolean scrape() throws IOException, InterruptedException {
        newsService.scrape();
        newsService.scrapeNewsReactions();
        log.info("-------------------------- scraping finished! --------------------------");
        return true;
    }
}
