package com.birdseyeapi.birdseyeapi.ScrapingNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.birdseyeapi.birdseyeapi.News;

public class ScrapeZenn implements ScrapingBase {
    private final Logger LOG = LogManager.getLogger();
    private final String SOURCE_BY = "zenn";
    private final String SOURCE_URL = "https://zenn.dev";

    @Override
    public String getSourceBy() {
        return SOURCE_BY;
    }

    @Override
    public List<News> extractNews() throws IOException {
        List<News> newsList = new ArrayList<News>();

        // jsoupで解析
        Document doc = Jsoup.connect(SOURCE_URL).get();
        Elements newsAreaList = doc.select("#tech-trend > div > div > div > div > article > div[class^=\"ArticleList_content\"] > a[class^=\"ArticleList_link\"]");
        for (Element newsArea : newsAreaList) {
            Elements newsTitle = newsArea.select("h2");
            String href = SOURCE_URL + newsArea.attr("href");
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            News news = new News();
            news.title = newsTitle.text();
            news.description = null;
            news.sourceBy = SOURCE_BY;
            news.scrapedUrl = SOURCE_URL;
            news.scrapedDateTime = now;
            news.articleUrl = href;
            news.articleImageUrl = null;
            newsList.add(news);
        }

        return newsList;
    }        
}
