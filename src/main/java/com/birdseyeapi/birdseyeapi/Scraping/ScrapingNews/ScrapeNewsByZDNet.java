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
public class ScrapeNewsByZDNet extends ScrapingNews {
    private static final String SOURCE_BY = "zdnet";
    private static final String SOURCE_URL = "https://japan.zdnet.com";

    public ScrapeNewsByZDNet(SummarizeNews summarizeNews) {
        super(summarizeNews);
    }

    @Override
    public String getSourceBy() {
        return SOURCE_BY;
    }

    @Override
    public Elements getDomElements() throws IOException {
        Document doc = Jsoup.connect(SOURCE_URL).get();
        return doc.select("#page-wrap > div.pg-container-main > main > section:nth-child(1) > div > ul > li");
    }

    @Override
    public News generateNews(Element newsArea) {
        String href = SOURCE_URL + newsArea.select("a").attr("href");
        String newsTitle = newsArea.select("a > div.txt > p.txt-ttl").text();
        Elements newsImage = newsArea.select("a > div.thumb > img");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        News news = new News();
        news.setTitle(newsTitle);
        news.setDescription(null);
        news.setSourceBy(SOURCE_BY);
        news.setScrapedUrl(SOURCE_URL);
        news.setScrapedDateTime(now);
        news.setArticleUrl(href);
        news.setArticleImageUrl(SOURCE_URL + newsImage.attr("src"));
        return news;
    }
}
