package com.birdseyeapi.birdseyeapi.SiteScraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.birdseyeapi.birdseyeapi.News;

public class ScrapeAtMarkIt implements ScrapingBase {
    private final Logger LOG = LogManager.getLogger();
    private final String SOURCE_BY = "atMarkItNews";
    private final String SOURCE_URL = "https://atmarkit.itmedia.co.jp/ait/subtop/news";

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
        int id = 0;
        for (Element newsArea : newsAreaList) {
            id++;
            Elements newsTitle = newsArea.select("div.colBoxTitle > h3");
            Elements newsDescription = newsArea.select("div.colBoxDescription > p");
            Elements newsImage = newsArea.select("div.colBoxIcon > a > img");

            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            String nowString = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            newsList.add(new News(id, newsTitle.text(), newsDescription.text(), SOURCE_BY, SOURCE_URL, nowString, newsTitle.select("a").attr("href"), newsImage.attr("src")));
        }

        return newsList;
    }
}
