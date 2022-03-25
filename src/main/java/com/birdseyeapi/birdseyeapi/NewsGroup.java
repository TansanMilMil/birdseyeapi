package com.birdseyeapi.birdseyeapi;

import java.util.List;

public class NewsGroup {
    public String sourceBy;
    public String scrapedUrl;
    public List<News> news;

    public NewsGroup(String sourceBy, String scrapedUrl, List<News> news) {
        this.sourceBy = sourceBy;
        this.scrapedUrl = scrapedUrl;
        this.news = news;
    }
}
