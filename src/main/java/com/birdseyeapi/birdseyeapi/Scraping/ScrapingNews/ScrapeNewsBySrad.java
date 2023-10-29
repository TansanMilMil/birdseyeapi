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
public class ScrapeNewsBySrad extends ScrapingNews {
    private static final String SOURCE_BY = "srad";
    private static final String SOURCE_URL = "https://srad.jp";

    public ScrapeNewsBySrad(SummarizeNews summarizeNews) {
        super(summarizeNews);
    }

    @Override
    public String getSourceBy() {
        return SOURCE_BY;
    }

    @Override
    public Elements getDomElements() throws IOException {
        Document doc = Jsoup.connect(SOURCE_URL).get();
        return doc.select("#firehoselist > article");
    }

    @Override
    public News generateNews(Element newsArea) {
        Elements newsTitle = newsArea.select("header > h2.story > span[id^=\"title\"] > a");
        Elements newsDescription = newsArea.select("div.body > div");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        News news = new News();
        news.setTitle(newsTitle.text());
        news.setDescription(newsDescription.html());
        news.setSourceBy(SOURCE_BY);
        news.setScrapedUrl(SOURCE_URL);
        news.setScrapedDateTime(now);
        news.setArticleUrl("https:" + newsTitle.attr("href"));
        news.setArticleImageUrl(null);
        return news;
    }
}
