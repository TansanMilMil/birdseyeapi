package com.birdseyeapi.birdseyeapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.birdseyeapi.birdseyeapi.AI.Summarize.AISummarizer;
import com.rometools.rome.io.FeedException;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("news")
public class NewsController {
    private final NewsService newsService;
    private final AISummarizer aiSummarizer;

    @GetMapping("/today-news")
    public List<NewsWithReactionCount> getTodayNews() throws IOException {
        List<NewsWithReactionCount> newsList = newsService.getTodayNews();
        return newsList;
    }

    @GetMapping("/trends")
    public List<News> getTrends() throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
        List<News> newsList = newsService.getTrends();
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
        return true;
    }
    
    @PostMapping("/summarize")
    public String summarize(@RequestBody NewsSummarizeReqest body) throws IOException, InterruptedException {
        String summarizeText = aiSummarizer.summarize(body.getText());
        return summarizeText;
    }
}
