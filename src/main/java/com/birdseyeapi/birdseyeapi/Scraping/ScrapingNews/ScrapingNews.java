package com.birdseyeapi.birdseyeapi.Scraping.ScrapingNews;

import java.io.IOException;
import java.util.List;

import com.birdseyeapi.birdseyeapi.News;

public interface ScrapingNews {
    public abstract String getSourceBy();

    public abstract List<News> extractNews() throws IOException;
}
