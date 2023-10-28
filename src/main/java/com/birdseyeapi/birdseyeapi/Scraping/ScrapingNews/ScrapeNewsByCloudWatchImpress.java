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
public class ScrapeNewsByCloudWatchImpress implements ScrapingNews {
    private final String SOURCE_BY = "cloudWatchImpress";
    private final String SOURCE_URL = "https://cloud.watch.impress.co.jp";
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
        Elements newsAreaList = doc.select("li.item.news");
        for (Element newsArea : newsAreaList) {
            Elements newsTitle = newsArea.select("p.title > a");
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            News news = new News();
            news.title = newsTitle.text();
            news.description = null;
            news.sourceBy = SOURCE_BY;
            news.scrapedUrl = SOURCE_URL;
            news.scrapedDateTime = now;
            news.articleUrl = newsTitle.attr("href");
            // CloudWatchImpress URL sometimes has protocol string.
            if (!news.articleUrl.matches("^https:\\/\\/.*")) {
                news.articleUrl = SOURCE_URL + news.articleUrl;
            }
            news.articleImageUrl = null;
            news.summarizedText = summarizeNews.summarize(news.articleUrl);
            newsList.add(news);

            log.info("scraped: " + news.title);
        }

        return newsList;
    }
}
