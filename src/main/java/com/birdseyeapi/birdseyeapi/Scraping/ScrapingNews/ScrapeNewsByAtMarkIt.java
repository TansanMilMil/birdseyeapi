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
public class ScrapeNewsByAtMarkIt extends ScrapingNews {
    private static final String SOURCE_BY = "atMarkItNews";
    private static final String SOURCE_URL = "https://atmarkit.itmedia.co.jp/ait/subtop/news";

    public ScrapeNewsByAtMarkIt(SummarizeNews summarizeNews) {
        super(summarizeNews);
    }

    @Override
    public String getSourceBy() {
        return SOURCE_BY;
    }

    @Override
    public Elements getDomElements() throws IOException {
        Document doc = Jsoup.connect(SOURCE_URL).get();
        return doc.select("#subtopContents > div:nth-child(3) > div > div.colBoxInner > div");
    }

    @Override
    public News generateNews(Element newsArea) {
        Elements newsTitle = newsArea.select("div.colBoxTitle > h3");
        Elements newsDescription = newsArea.select("div.colBoxDescription > p");
        Elements newsImage = newsArea.select("div.colBoxIcon > a > img");

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        News news = new News();
        news.setTitle(newsTitle.text());
        news.setDescription(newsDescription.text());
        news.setSourceBy(SOURCE_BY);
        news.setScrapedUrl(SOURCE_URL);
        news.setScrapedDateTime(now);
        news.setArticleUrl(newsTitle.select("a").attr("href"));
        news.setArticleImageUrl(newsImage.attr("src"));
        return news;
    }
}
