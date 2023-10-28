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

@Component
@RequiredArgsConstructor
public class ScrapeNewsByGigazine implements ScrapingNews {
    private final String SOURCE_BY = "gigazine";
    private final String SOURCE_URL = "https://gigazine.net";
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
        Elements newsAreaList = doc.select("#section > div > section > div.card");
        for (Element newsArea : newsAreaList) {
            Elements newsTitle = newsArea.select("h2 > a");
            Elements newsImage = newsArea.select("div.thumb > a > img");
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            String linkHref = newsTitle.attr("href");
            String description = null;
            if (linkHref != null) {
                Elements metaTags = Jsoup.connect(linkHref).get().getElementsByTag("meta");
                for (Element metaTag : metaTags) {
                    String name = metaTag.attr("name");
                    String content = metaTag.attr("content");
                    if ("description".equals(name)) {
                        description = content;
                    }
                }
            }
            News news = new News();
            news.title = newsTitle.text();
            news.description = description;
            news.sourceBy = SOURCE_BY;
            news.scrapedUrl = SOURCE_URL;
            news.scrapedDateTime = now;
            news.articleUrl = linkHref;
            news.articleImageUrl = newsImage.attr("src");
            news.summarizedText = summarizeNews.summarize(news.articleUrl);
            newsList.add(news);
        }

        return newsList;
    }
}
