package com.birdseyeapi.birdseyeapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.birdseyeapi.birdseyeapi.SiteScraping.SiteScraping;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {
    @Autowired
    private NewsRepository newsRepostiroy;
    private final String GOOGLE_RSS_TRENDS_DAILY = "https://trends.google.co.jp/trends/trendingsearches/daily/rss?geo=JP";

    public List<News> getTodayNews() throws IOException {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        ZonedDateTime today = ZonedDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 0, 0, 0, 0, now.getZone());
        System.out.println(today.toString());
        List<News> newsList = newsRepostiroy.findByscrapedDateTimeGreaterThanEqual(today);
        Collections.shuffle(newsList);
        return newsList;
    }

    public List<News> getTrends() throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
        String SOURCE_BY = "googleTrends";
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(GOOGLE_RSS_TRENDS_DAILY)));
        List<News> newsList = new ArrayList<News>();
        for (SyndEntry entry : feed.getEntries()) {
            String description = null;
            String articleUrl = null;
            String articleImageUrl = null;
            for (Element element : entry.getForeignMarkup()) {
                System.out.print(element.getName() + ": " + element.getValue() + "\n");
                if (element.getName() == "news_item") {
                    for (Element childElement : element.getChildren()) {
                        if (childElement.getName() == "news_item_snippet") {
                            description = childElement.getValue();
                        }
                        if (childElement.getName() == "news_item_url") {
                            articleUrl = childElement.getValue();
                        }
                    }
                    
                }
                if (element.getName() == "picture") {
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

    public boolean scrape() throws IOException {
        List<News> newsList = SiteScraping.scrape();
        newsRepostiroy.saveAll(newsList);
        return true;
    }
}
