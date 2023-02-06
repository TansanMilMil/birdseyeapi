package com.birdseyeapi.birdseyeapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private NewsReactionRepository newsReactionRepository;

    private final String GOOGLE_RSS_TRENDS_DAILY = "https://trends.google.co.jp/trends/trendingsearches/daily/rss?geo=JP";

    public List<NewsWithReactionCount> getTodayNews() throws IOException {
        Long maxScrapingUnitId = newsReactionRepository.findMaxScrapingUnitId();
        List<NewsWithReactionCount> newsList = newsReactionRepository.selectNewsWithReactionCount(maxScrapingUnitId);
        Collections.shuffle(newsList);
        return newsList;
    }

    public List<News> getTrends() throws IllegalArgumentException, FeedException, IOException {
        List<News> newsList = new ArrayList<>();

        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(GOOGLE_RSS_TRENDS_DAILY)));
        for (SyndEntry entry : feed.getEntries()) {
            newsList.add(entryToNews(entry));
        }

        return newsList;
    }

    private News entryToNews(SyndEntry entry) {
        News news = new News();
        news.title = entry.getTitle();
        news.scrapedUrl = entry.getLink();
        news.sourceBy = "googleTrends";
        news.scrapedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));

        for (Element element : entry.getForeignMarkup()) {
            System.out.print(element.getName() + ": " + element.getValue() + "\n");
            if ("news_item".equals(element.getName())) {
                for (Element childElement : element.getChildren()) {
                    if ("news_item_snippet".equals(childElement.getName())) {
                        news.description = childElement.getValue();
                    }
                    if ("news_item_url".equals(childElement.getName())) {
                        news.articleUrl = childElement.getValue();
                    }
                }

            }
            if ("picture".equals(element.getName())) {
                news.articleImageUrl = element.getValue();
            }

        }
        return news;
    }

    public List<NewsReaction> getNewsReactions(long id) {
        List<News> result = newsReactionRepository.selectNewsReactionsById(id);
        if (result.size() == 0) {
            log.info("no news!");
            return new ArrayList<NewsReaction>();
        } else {
            return result.get(0).reactions;
        }
    }

    @Transactional
    public boolean scrape() {
        List<News> newsList = SiteScraping.scrapeNews();

        Long maxScrapingUnitId = newsReactionRepository.findMaxScrapingUnitId();
        newsList = newsList.stream().map(news -> {
            news.scrapingUnitId = maxScrapingUnitId == null ? 1 : maxScrapingUnitId + 1;
            return news;
        }).toList();
        newsRepository.saveAll(newsList);
        return true;
    }

    public boolean scrapeNewsReactions() throws InterruptedException, MalformedURLException {
        Long maxScrapingUnitId = newsReactionRepository.findMaxScrapingUnitId();

        List<News> newsList = newsReactionRepository.selectNewsReactionsByScrapingUnitId(maxScrapingUnitId);

        if (newsList.size() == 0) {
            log.info("no news!");
            return false;
        }

        for (News news : newsList) {
            saveReactions(news);
        }
        return true;
    }

    @Transactional
    private void saveReactions(News news) throws MalformedURLException, InterruptedException {
        log.info("news.id:" + news.id);
        if (news.reactions != null && news.reactions.size() >= 1) {
            log.info("exist reactions in database.");
            return;
        }

        List<NewsReaction> reactions = SiteScraping.scrapeReactions(news);
        reactions = reactions.stream().map(reaction -> {
            reaction.news = news;
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
