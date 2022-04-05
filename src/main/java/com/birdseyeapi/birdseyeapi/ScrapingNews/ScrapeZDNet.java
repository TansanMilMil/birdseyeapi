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

public class ScrapeZDNet implements ScrapingBase {
    private final Logger LOG = LogManager.getLogger();
    private final String SOURCE_BY = "zdnet";
    private final String SOURCE_URL = "https://japan.zdnet.com";

    @Override
    public String getSourceBy() {
        return SOURCE_BY;
    }

    @Override
    public List<News> extractNews() throws IOException {
        List<News> newsList = new ArrayList<News>();

        // jsoupで解析
        Document doc = Jsoup.connect(SOURCE_URL).get();
        Elements newsAreaList = doc.select("#page-wrap > div.pg-container-main > main > section:nth-child(1) > div > ul > li");
        for (Element newsArea : newsAreaList) {
            String href = SOURCE_URL + newsArea.select("a").attr("href");
            String newsTitle = newsArea.select("a > div.txt > p.txt-ttl").text();
            Elements newsImage = newsArea.select("a > div.thumb > img");
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            News news = new News();
            news.title = newsTitle;
            news.description = null;
            news.sourceBy = SOURCE_BY;
            news.scrapedUrl = SOURCE_URL;
            news.scrapedDateTime = now;
            news.articleUrl = href;
            news.articleImageUrl = SOURCE_URL + newsImage.attr("src");
            newsList.add(news);
        }

        return newsList;
    }            
}
