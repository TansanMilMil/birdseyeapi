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
        int id = 0;
        for (Element newsArea : newsAreaList) {
            id++;
            String href = SOURCE_URL + newsArea.select("a").attr("href");
            String newsTitle = newsArea.select("a > div.txt > p.txt-ttl").text();
            Elements newsImage = newsArea.select("a > div.thumb > img");
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
            String nowString = now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            newsList.add(new News(id, newsTitle, null, SOURCE_BY, SOURCE_URL, nowString, href, SOURCE_URL + newsImage.attr("src")));
        }

        return newsList;
    }            
}
