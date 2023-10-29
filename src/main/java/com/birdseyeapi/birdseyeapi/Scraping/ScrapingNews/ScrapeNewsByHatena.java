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
public class ScrapeNewsByHatena implements ScrapingNews {
    private final String SOURCE_BY = "hatena";
    private final String SOURCE_URL = "https://b.hatena.ne.jp/hotentry/it";
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
        Elements newsAreaList = doc.select("#container .entrylist-contents-main");
        for (Element newsArea : newsAreaList) {
            Elements newsTitle = newsArea.select(".entrylist-contents-title > a");
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
            news.setTitle(newsTitle.text());
            news.setDescription(description);
            news.setSourceBy(SOURCE_BY);
            news.setScrapedUrl(SOURCE_URL);
            news.setScrapedDateTime(now);
            news.setArticleUrl(linkHref);
            news.setArticleImageUrl(null);
            news.setSummarizedText(summarizeNews.summarize(news.articleUrl));
            newsList.add(news);

            log.info("scraped: " + news.title);
        }

        return newsList;
    }
}
