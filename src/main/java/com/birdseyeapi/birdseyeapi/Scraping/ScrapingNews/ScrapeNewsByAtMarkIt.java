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

import com.birdseyeapi.birdseyeapi.News.News;
import com.birdseyeapi.birdseyeapi.Scraping.SummarizeNews.SummarizeNews;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        log.info("scrape " + SOURCE_BY);
        Document doc = Jsoup.connect(SOURCE_URL).get();
        Elements newsAreaList = doc.select("#subtopContents > div:nth-child(3) > div > div.colBoxInner > div");
        for (Element newsArea : newsAreaList) {
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
            news.setSummarizedText(summarizeNews.summarize(news.articleUrl));
            newsList.add(news);

            log.info("scraped: " + news.title);
        }

        return newsList;
    }
}
