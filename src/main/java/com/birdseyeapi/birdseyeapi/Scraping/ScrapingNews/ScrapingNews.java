package com.birdseyeapi.birdseyeapi.Scraping.ScrapingNews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.birdseyeapi.birdseyeapi.News.News;
import com.birdseyeapi.birdseyeapi.Scraping.SummarizeNews.SummarizeNews;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@SuperBuilder
public abstract class ScrapingNews {
    private final SummarizeNews summarizeNews;
    private final int MAX_NEWS = 10;

    public abstract String getSourceBy();

    public abstract Elements getDomElements() throws IOException;

    public abstract News generateNews(Element newsArea);

    private News addSummarizedText(News news) {
        news.setSummarizedText(summarizeNews.summarize(news.getArticleUrl()));
        return news;
    }

    public final List<News> extractNews() throws IOException {
        log.info("scraping... -> " + getSourceBy());
        List<Element> newsAreaList = getDomElements().stream().toList();
        // Sometimes, There are too many news.
        if (newsAreaList.size() > MAX_NEWS) {
            newsAreaList = newsAreaList.subList(0, MAX_NEWS);
        }

        final List<News> newsList = new ArrayList<News>();
        for (Element newsArea : newsAreaList) {
            News news = generateNews(newsArea);
            news = addSummarizedText(news);
            newsList.add(news);
        }

        log.info("scraped! -> " + getSourceBy());
        return newsList;
    }
}
