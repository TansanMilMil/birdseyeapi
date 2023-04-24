package com.birdseyeapi.birdseyeapi;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.birdseyeapi.birdseyeapi.ScrapingNews.ScrapeNewsByAtMarkIt;
import com.birdseyeapi.birdseyeapi.ScrapingNews.ScrapeNewsByCloudWatchImpress;
import com.birdseyeapi.birdseyeapi.ScrapingNews.ScrapeNewsByHatena;
import com.birdseyeapi.birdseyeapi.ScrapingNews.ScrapeNewsBySrad;
import com.birdseyeapi.birdseyeapi.ScrapingNews.ScrapeNewsByZDNet;
import com.birdseyeapi.birdseyeapi.ScrapingNews.ScrapeNewsByZenn;
import com.birdseyeapi.birdseyeapi.ScrapingNews.ScrapingNews;
import com.birdseyeapi.birdseyeapi.ScrapingReaction.ScrapeReactionsByHatena;
import com.birdseyeapi.birdseyeapi.ScrapingReaction.ScrapeReactionsByTwitter;
import com.birdseyeapi.birdseyeapi.ScrapingReaction.ScrapingReaction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SiteScraping {
    public static List<News> scrapeNews() {
        List<ScrapingNews> targets = List.of(
                new ScrapeNewsByAtMarkIt(),
                new ScrapeNewsByCloudWatchImpress(),
                new ScrapeNewsByHatena(),
                new ScrapeNewsByZenn(),
                new ScrapeNewsBySrad(),
                // new ScrapeGigazine(),
                new ScrapeNewsByZDNet());
        List<News> newsList = new ArrayList<>();
        for (ScrapingNews target : targets) {
            try {
                List<News> list = target.extractNews();
                list = list.subList(0, 10);
                log.info(target.getSourceBy() + " -> scraped article: " + list.size());
                newsList.addAll(list);
            } catch (Exception e) {
                // continue scraping even if it occur exception.
                log.error(target.getSourceBy() + " -> scraped failed...");
                log.error(e.getMessage());
                log.error(e.getStackTrace().toString());
            }
        }
        return newsList;
    }

    public static List<NewsReaction> scrapeReactions(News news) throws MalformedURLException, InterruptedException {
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
