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
public class ScrapeNewsByHatena extends ScrapingNews {
    private static final String SOURCE_BY = "hatena";
    private static final String SOURCE_URL = "https://b.hatena.ne.jp/hotentry/it";

    public ScrapeNewsByHatena(SummarizeNews summarizeNews) {
        super(summarizeNews);
    }

    @Override
    public String getSourceBy() {
        return SOURCE_BY;
    }

    @Override
    public Elements getDomElements() throws IOException {
        Document doc = Jsoup.connect(SOURCE_URL).get();
        return doc.select("#container .entrylist-contents-main");
    }

    @Override
    public News generateNews(Element newsArea) {
        Elements newsTitle = newsArea.select(".entrylist-contents-title > a");
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        String linkHref = newsTitle.attr("href");
        String description = null;
        if (linkHref != null) {
            Elements metaTags;
            try {
                metaTags = Jsoup.connect(linkHref).get().getElementsByTag("meta");
                for (Element metaTag : metaTags) {
                    String name = metaTag.attr("name");
                    String content = metaTag.attr("content");
                    if ("description".equals(name)) {
                        description = content;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
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
        return news;
    }
}
