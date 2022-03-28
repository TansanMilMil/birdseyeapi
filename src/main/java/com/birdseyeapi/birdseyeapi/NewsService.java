package com.birdseyeapi.birdseyeapi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.birdseyeapi.birdseyeapi.AwsS3.S3Manager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.model.S3Object;

@Service
public class NewsService {

    @Autowired
    private S3Manager s3Manager;
    private final String BUCKET_NAME = "birds-eye-news";
    private final String GOOGLE_RSS_TRENDS_DAILY = "https://trends.google.co.jp/trends/trendingsearches/daily/rss?geo=JP";

    public List<News> getTodayNews() throws IOException {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        String prefix = now.format(DateTimeFormatter.ISO_LOCAL_DATE);
        List<S3Object> s3Objects = s3Manager.listObjects(BUCKET_NAME, prefix);
        List<Integer> targetIndexes = new ArrayList<Integer>();
        for (int i = 0; i < s3Objects.size(); i++) {
            targetIndexes.add(i);
        }
        Collections.shuffle(targetIndexes);
        targetIndexes = targetIndexes.subList(0, 19);

        List<News> newsList = new ArrayList<News>();
        for (Integer targetIndex : targetIndexes) {
            String json = s3Manager.getJsonObject(BUCKET_NAME, s3Objects.get(targetIndex).key());
            ObjectMapper mapper = new ObjectMapper();
            News news = mapper.readValue(json, News.class);
            newsList.add(news);
        }

        return newsList;
    }

    public List<News> getTrends() throws IllegalArgumentException, MalformedURLException, FeedException, IOException {
        String SOURCE_BY = "googleTrends";
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        String nowString = now.format(DateTimeFormatter.ISO_LOCAL_DATE);

        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(GOOGLE_RSS_TRENDS_DAILY)));
        List<News> newsList = new ArrayList<News>();
        for (SyndEntry entry : feed.getEntries()) {
            String description = null;
            String articleImageUrl = null;
            for (Element element : entry.getForeignMarkup()) {
                System.out.print(element.getName() + ": " + element.getValue() + "\n");
                if (element.getName() == "news_item") {
                    description = element.getValue();
                }
                if (element.getName() == "picture") {
                    articleImageUrl = element.getValue();
                }
                
            }
            var news = new News();
            news.id = 0;
            news.title = entry.getTitle();
            news.description = description;
            news.sourceBy = SOURCE_BY;
            news.scrapedUrl = GOOGLE_RSS_TRENDS_DAILY;
            news.scrapedDateTime = nowString;
            news.articleUrl = entry.getLink();
            news.articleImageUrl = articleImageUrl;          
            newsList.add(news);
        }
        return newsList;
    }
}
