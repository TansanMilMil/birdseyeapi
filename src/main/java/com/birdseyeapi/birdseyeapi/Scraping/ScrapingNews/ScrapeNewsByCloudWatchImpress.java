package com.birdseyeapi.birdseyeapi.Scraping.ScrapingNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import com.birdseyeapi.birdseyeapi.News.News;
import com.birdseyeapi.birdseyeapi.Scraping.SummarizeNews.SummarizeNews;

@Component
public class ScrapeNewsByCloudWatchImpress extends ScrapingNews {
    private final String SOURCE_BY = "cloudWatchImpress";
    private final String SOURCE_URL = "https://cloud.watch.impress.co.jp";

    public ScrapeNewsByCloudWatchImpress(SummarizeNews summarizeNews) {
        super(summarizeNews);
    }

    @Override
    public String getSourceBy() {
        return SOURCE_BY;
    }

    @Override
    public Elements getDomElements() throws IOException {
        Document doc = Jsoup.connect(SOURCE_URL).get();
        return doc.select("li.item.news");
    }

    @Override
    public News generateNews(Element newsArea) {
        Elements newsTitle = newsArea.select("p.title > a");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        News news = new News();
        news.setTitle(newsTitle.text());
        news.setDescription(null);
        news.setSourceBy(SOURCE_BY);
        news.setScrapedUrl(SOURCE_URL);
        news.setScrapedDateTime(now);
        news.setArticleUrl(newsTitle.attr("href"));
        // CloudWatchImpress URL sometimes has no protocol string.
        if (!news.getArticleUrl().matches("^https:\\/\\/.*")) {
            news.setArticleUrl(SOURCE_URL + news.getArticleUrl());
        }
        news.setArticleImageUrl(null);
        return news;
    }
}
