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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.birdseyeapi.birdseyeapi.ScrapingReaction.ScrapeReactionsByHatena;
import com.birdseyeapi.birdseyeapi.ScrapingReaction.ScrapeReactionsByTwitter;
import com.birdseyeapi.birdseyeapi.ScrapingNews.SiteScraping;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import java.util.Random;
import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private NewsReactionRepository newsReactionRepository;
    @PersistenceContext
    EntityManager em;
    private final String GOOGLE_RSS_TRENDS_DAILY = "https://trends.google.co.jp/trends/trendingsearches/daily/rss?geo=JP";
    private static final Logger LOG = LogManager.getLogger();

    public List<NewsWithReactionCount> getTodayNews() throws IOException {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime today = ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0, 0, 0, now.getZone());

        Long maxScrapingUnitId = em.createQuery("""
                SELECT MAX(n.scrapingUnitId)
                FROM News n
            """, Long.class)
            .getSingleResult();
        List<NewsWithReactionCount> newsList = em.createQuery("""
                SELECT 
                    NEW com.birdseyeapi.birdseyeapi.NewsWithReactionCount(
                        n.id
                        , MAX(n.title)
                        , MAX(n.description)
                        , MAX(n.sourceBy)
                        , MAX(n.scrapedUrl)
                        , MAX(n.scrapedDateTime)
                        , MAX(n.articleUrl)
                        , MAX(n.articleImageUrl)
                        , COUNT(r.id)
                    )
                FROM News n 
                LEFT JOIN n.reactions r
                WHERE n.scrapingUnitId >= :maxScrapingUnitId
                GROUP BY n.id
            """, NewsWithReactionCount.class)
            .setParameter("maxScrapingUnitId", maxScrapingUnitId)
            .getResultList();
        Collections.shuffle(newsList);
        return newsList;
    }

    public List<News> getTrends() throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
        String SOURCE_BY = "googleTrends";
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(GOOGLE_RSS_TRENDS_DAILY)));
        List<News> newsList = new ArrayList<>();
        for (SyndEntry entry : feed.getEntries()) {
            String description = null;
            String articleUrl = null;
            String articleImageUrl = null;
            for (Element element : entry.getForeignMarkup()) {
                System.out.print(element.getName() + ": " + element.getValue() + "\n");
                if ("news_item".equals(element.getName())) {
                    for (Element childElement : element.getChildren()) {
                        if ("news_item_snippet".equals(childElement.getName())) {
                            description = childElement.getValue();
                        }
                        if ("news_item_url".equals(childElement.getName())) {
                            articleUrl = childElement.getValue();
                        }
                    }
                    
                }
                if ("picture".equals(element.getName())) {
                    articleImageUrl = element.getValue();
                }
                
            }
            News news = new News();
            news.title = entry.getTitle();
            news.description = description;
            news.sourceBy = SOURCE_BY;
            news.scrapedUrl = entry.getLink();
            news.scrapedDateTime = now;
            news.articleUrl = articleUrl;
            news.articleImageUrl = articleImageUrl;
            newsList.add(news);
        }
        return newsList;
    }

    public List<NewsReaction> getNewsReactions(long id) throws IOException, InterruptedException {
        List<News> result = em.createQuery("""
                SELECT n
                FROM News n 
                LEFT JOIN FETCH n.reactions
                WHERE n.id = :id
            """, News.class)
            .setParameter("id", id)
            .getResultList();
        if (result.size() == 0) {
            LOG.info("no news!");
            return new ArrayList<NewsReaction>();
        } else {
            return result.get(0).reactions;
        }
    }
    
    @Transactional
    public boolean scrape() throws IOException {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime today = ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0, 0, 0, now.getZone());

        List<News> newsList = SiteScraping.scrape();
        Long maxScrapingUnitId = em.createQuery("""
                SELECT MAX(n.scrapingUnitId)
                FROM News n
            """, Long.class)
                .getSingleResult();
        newsList = newsList.stream().map(news -> {
            news.scrapingUnitId = maxScrapingUnitId + 1;
            return news;
        }).toList();
        newsRepository.saveAll(newsList);
        return true;
    }
    
    public boolean scrapeNewsReactions() throws InterruptedException, MalformedURLException {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime today = ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0, 0, 0, now.getZone());
        Long maxScrapingUnitId = em.createQuery("""
                SELECT MAX(n.scrapingUnitId)
                FROM News n
            """, Long.class)
            .getSingleResult();

        List<News> newsList = em.createQuery("""
                SELECT n
                FROM News n 
                LEFT JOIN FETCH n.reactions
                WHERE n.scrapingUnitId = :maxScrapingUnitId
            """, News.class)
            .setParameter("maxScrapingUnitId", maxScrapingUnitId)
            .getResultList();
        if (newsList.size() == 0) {
            LOG.info("no news!");
            return false;
        }
        
        for (News news : newsList) {
            saveReactions(news);
        }
        return true;
    }

    @Transactional
    private void saveReactions(News news) throws MalformedURLException, InterruptedException {
        LOG.info("news.id:" + news.id);
        if (news.reactions != null && news.reactions.size() >= 1) {
            LOG.info("exist reactions in database.");
            return;
        }
        
        LOG.info("scraping...");
        List<NewsReaction> reactions = new ArrayList<>();
        reactions.addAll(ScrapeReactionsByTwitter.extractReactions(news.articleUrl, news.title));
        reactions.addAll(ScrapeReactionsByHatena.extractReactions(news.articleUrl, news.title));
        reactions = reactions.stream().map(reaction -> {
            reaction.news = news;
            return reaction;
        }).toList();
        if (reactions.size() >= 1) {
            LOG.info("save reactions.");
            newsReactionRepository.saveAll(reactions);
        }
        
        // wait as random between 0 ~ 100 ms.
        int sleepTime = new Random().nextInt(101);
        Thread.sleep(sleepTime);
    }
}
