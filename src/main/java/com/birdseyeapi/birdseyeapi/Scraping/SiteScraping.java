package com.birdseyeapi.birdseyeapi.Scraping;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.birdseyeapi.birdseyeapi.News;
import com.birdseyeapi.birdseyeapi.NewsReaction;
import com.birdseyeapi.birdseyeapi.Scraping.ScrapingNews.ScrapeNewsByAtMarkIt;
import com.birdseyeapi.birdseyeapi.Scraping.ScrapingNews.ScrapeNewsByCloudWatchImpress;
import com.birdseyeapi.birdseyeapi.Scraping.ScrapingNews.ScrapeNewsByHatena;
import com.birdseyeapi.birdseyeapi.Scraping.ScrapingNews.ScrapeNewsBySrad;
import com.birdseyeapi.birdseyeapi.Scraping.ScrapingNews.ScrapeNewsByZDNet;
import com.birdseyeapi.birdseyeapi.Scraping.ScrapingNews.ScrapeNewsByZenn;
import com.birdseyeapi.birdseyeapi.Scraping.ScrapingNews.ScrapingNews;
import com.birdseyeapi.birdseyeapi.Scraping.ScrapingReaction.ScrapeReactionsByHatena;
import com.birdseyeapi.birdseyeapi.Scraping.ScrapingReaction.ScrapeReactionsByTwitter;
import com.birdseyeapi.birdseyeapi.Scraping.ScrapingReaction.ScrapingReaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SiteScraping {
    private final ScrapeNewsByAtMarkIt scrapeNewsByAtMarkIt;
    private final ScrapeNewsByCloudWatchImpress scrapeNewsByCloudWatchImpress;
    private final ScrapeNewsByHatena scrapeNewsByHatena;
    private final ScrapeNewsByZenn scrapeNewsByZenn;
    private final ScrapeNewsBySrad scrapeNewsBySrad;
    // private final ScrapeNewsByGigazine scrapeNewsByGigazine;
    private final ScrapeNewsByZDNet scrapeNewsByZDNet;

    public List<News> scrapeNews() {
        List<ScrapingNews> targets = List.of(
                scrapeNewsByAtMarkIt,
                scrapeNewsByCloudWatchImpress,
                scrapeNewsByHatena,
                scrapeNewsByZenn,
                scrapeNewsBySrad,
                // scrapeGigazine,
                scrapeNewsByZDNet);
        List<News> newsList = new ArrayList<>();
        for (ScrapingNews target : targets) {
            try {
                List<News> list = target.extractNews();
                // Sometimes, There are too many news.
                if (list.size() > 10) {
                    list = list.subList(0, 10);
                }
                log.info(target.getSourceBy() + " -> scraped article: " + list.size());
                newsList.addAll(list);
            } catch (Exception e) {
                // continue scraping even if it occur exception.
                log.error(target.getSourceBy() + " -> scraped failed...");
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
        return newsList;
    }

    public List<NewsReaction> scrapeReactions(News news) throws MalformedURLException, InterruptedException {
        List<ScrapingReaction> targets = List.of(
                new ScrapeReactionsByTwitter(),
                new ScrapeReactionsByHatena());
        List<NewsReaction> reactions = new ArrayList<>();

        for (ScrapingReaction target : targets) {
            reactions.addAll(target.extractReactions(news.articleUrl, news.title));
        }

        return reactions;
    }
}