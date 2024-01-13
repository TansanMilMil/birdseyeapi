package com.birdseyeapi.birdseyeapi.Trends;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;
import org.springframework.stereotype.Service;
import com.birdseyeapi.birdseyeapi.News.News;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TrendsService {
    private final String GOOGLE_RSS_TRENDS_DAILY = "https://trends.google.co.jp/trends/trendingsearches/daily/rss?geo=JP";

    public List<News> getTrends()
            throws IllegalArgumentException, FeedException, IOException {
        List<News> newsList = new ArrayList<>();

        InputStream is = URI.create(GOOGLE_RSS_TRENDS_DAILY).toURL().openConnection().getInputStream();

        SyndFeed feed = new SyndFeedInput().build(new InputStreamReader(is, StandardCharsets.UTF_8));
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

}
