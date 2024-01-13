package com.birdseyeapi.birdseyeapi.News;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.birdseyeapi.birdseyeapi.Scraping.SiteScraping;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsReactionRepository newsReactionRepository;
    private final SiteScraping siteScraping;

    public List<NewsWithReactionCount> getTodayNews() throws IOException {
        Long maxScrapingUnitId = newsReactionRepository.findMaxScrapingUnitId();
        List<NewsWithReactionCount> newsList = newsReactionRepository.selectNewsWithReactionCount(maxScrapingUnitId);
        Collections.shuffle(newsList);
        return newsList;
    }

    public List<NewsReaction> getNewsReactions(long id) {
        List<NewsReaction> reactions = newsReactionRepository.selectNewsReactionsById(id)
                .stream()
                .map(reaction -> {
                    // avoid cross reference on response json
                    reaction.setNews(null);
                    return reaction;
                })
                .toList();
        if (reactions.size() == 0) {
            log.info("no news!");
            return new ArrayList<NewsReaction>();
        } else {
            return reactions;
        }
    }

    @Transactional
    public boolean scrape() {
        List<News> newsList = siteScraping.scrapeNews();

        Long maxScrapingUnitId = newsReactionRepository.findMaxScrapingUnitId();
        newsList = newsList.stream().map(news -> {
            if (maxScrapingUnitId == null) {
                news.setScrapingUnitId(1);
            } else {
                news.setScrapingUnitId(maxScrapingUnitId + 1);
            }
            return news;
        }).toList();
        newsRepository.saveAll(newsList);
        return true;
    }

    public boolean scrapeNewsReactions() throws InterruptedException, MalformedURLException {
        Long maxScrapingUnitId = newsReactionRepository.findMaxScrapingUnitId();

        List<News> newsList = newsReactionRepository.selectNewsByScrapingUnitId(maxScrapingUnitId);

        if (newsList.size() == 0) {
            log.info("no news!");
            return false;
        }

        for (News news : newsList) {
            scrapeAndSaveReactions(news);
        }
        return true;
    }

    @Transactional
    private void scrapeAndSaveReactions(News news) throws MalformedURLException, InterruptedException {
        log.info("news.id:" + news.getId());
        if (news.getReactions() != null && news.getReactions().size() >= 1) {
            log.info("exist reactions in database.");
            return;
        }

        List<NewsReaction> reactions = siteScraping.scrapeReactions(news);
        reactions = reactions.stream().map(reaction -> {
            reaction.setNews(news);
            return reaction;
        }).toList();
        if (reactions.size() >= 1) {
            log.info("save reactions.");
            newsReactionRepository.saveAll(reactions);
        }

        // delay between 0 ~ 100 ms to avoid any trouble on scraping target...
        int sleepTime = new Random().nextInt(101);
        Thread.sleep(sleepTime);
    }
}
