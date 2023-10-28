package com.birdseyeapi.birdseyeapi.Scraping.ScrapingNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.birdseyeapi.birdseyeapi.News;
import com.birdseyeapi.birdseyeapi.Scraping.SummarizeNews.SummarizeNews;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScrapeNewsBySrad implements ScrapingNews {
    private final String SOURCE_BY = "srad";
    private final String SOURCE_URL = "https://srad.jp";
    private final SummarizeNews summarizeNews;

    @Override
    public String getSourceBy() {
        return SOURCE_BY;
    }

    @Override
    public List<News> extractNews() throws IOException {
        List<News> newsList = new ArrayList<News>();

        // jsoupで解析
        log.info("scrape " + SOURCE_BY);
        Document doc = Jsoup.connect(SOURCE_URL).get();
        Elements newsAreaList = doc.select("#firehoselist > article");
        for (Element newsArea : newsAreaList) {
            Elements newsTitle = newsArea.select("header > h2.story > span[id^=\"title\"] > a");
            Elements newsDescription = newsArea.select("div.body > div");
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            News news = new News();
            news.title = newsTitle.text();
            news.description = newsDescription.html();
            news.sourceBy = SOURCE_BY;
            news.scrapedUrl = SOURCE_URL;
            news.scrapedDateTime = now;
            news.articleUrl = "https:" + newsTitle.attr("href");
            news.articleImageUrl = null;
            news.summarizedText = summarizeNews.summarize(news.articleUrl);
            newsList.add(news);

            log.info("scraped: " + news.title);
        }

        return newsList;
    }
}
