package com.birdseyeapi.birdseyeapi.News;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.birdseyeapi.birdseyeapi.Scraping.SiteScraping;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import org.jdom2.Element;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsReactionRepository newsReactionRepository;
    private final SiteScraping siteScraping;

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
        news.setTitle(entry.getTitle());
        news.setScrapedUrl(entry.getLink());
        news.setSourceBy("googleTrends");
        news.setScrapedDateTime(ZonedDateTime.now(ZoneId.of("UTC")));

        for (Element element : entry.getForeignMarkup()) {
            System.out.print(element.getName() + ": " + element.getValue() + "\n");
            if ("news_item".equals(element.getName())) {
                for (Element childElement : element.getChildren()) {
                    if ("news_item_snippet".equals(childElement.getName())) {
                        news.setDescription(childElement.getValue());
                    }
                    if ("news_item_url".equals(childElement.getName())) {
                        news.setArticleUrl(childElement.getValue());
                    }
                }

            }
            if ("picture".equals(element.getName())) {
                news.setArticleImageUrl(element.getValue());
            }

        }
        return news;
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