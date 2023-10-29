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
public class ScrapeNewsByZenn extends ScrapingNews {
    private static final String SOURCE_BY = "zenn";
    private static final String SOURCE_URL = "https://zenn.dev";

    public ScrapeNewsByZenn(SummarizeNews summarizeNews) {
        super(summarizeNews);
    }

    @Override
    public String getSourceBy() {
        return SOURCE_BY;
    }

    @Override
    public Elements getDomElements() throws IOException {
        Document doc = Jsoup.connect(SOURCE_URL).get();
        return doc.select(
                "#tech-trend > div > div > div > article > div > a[class^=\"ArticleList_link\"]");
    }

    @Override
    public News generateNews(Element newsArea) {
        Elements newsTitle = newsArea.select("h2");
        String href = SOURCE_URL + newsArea.attr("href");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        News news = new News();
        news.setTitle(newsTitle.text());
        news.setDescription(null);
        news.setSourceBy(SOURCE_BY);
        news.setScrapedUrl(SOURCE_URL);
        news.setScrapedDateTime(now);
        news.setArticleUrl(href);
        news.setArticleImageUrl(null);
        return news;
    }
}
