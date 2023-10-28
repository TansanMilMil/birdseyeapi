package com.birdseyeapi.birdseyeapi.Scraping.ScrapingNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.birdseyeapi.birdseyeapi.News;
import com.birdseyeapi.birdseyeapi.AI.Summarize.AISummarizer;
import com.birdseyeapi.birdseyeapi.Scraping.SummarizeNews.SummarizeNews;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScrapeNewsByAtMarkIt implements ScrapingNews {
    private final String SOURCE_BY = "atMarkItNews";
    private final String SOURCE_URL = "https://atmarkit.itmedia.co.jp/ait/subtop/news";
    private final SummarizeNews summarizeNews;

    @Override
    public String getSourceBy() {
        return SOURCE_BY;
    }

    @Override
    public List<News> extractNews() throws IOException {
        List<News> newsList = new ArrayList<News>();

        // jsoupで解析
        Document doc = Jsoup.connect(SOURCE_URL).get();
        Elements newsAreaList = doc.select("#subtopContents > div:nth-child(3) > div > div.colBoxInner > div");
        for (Element newsArea : newsAreaList) {
            Elements newsTitle = newsArea.select("div.colBoxTitle > h3");
            Elements newsDescription = newsArea.select("div.colBoxDescription > p");
            Elements newsImage = newsArea.select("div.colBoxIcon > a > img");

            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            News news = new News();
            news.title = newsTitle.text();
            news.description = newsDescription.text();
            news.sourceBy = SOURCE_BY;
            news.scrapedUrl = SOURCE_URL;
            news.scrapedDateTime = now;
            news.articleUrl = newsTitle.select("a").attr("href");
            news.articleImageUrl = newsImage.attr("src");
            news.summarizedText = summarizeNews.summarize(news.articleUrl);
            newsList.add(news);
        }

        return newsList;
    }
}
