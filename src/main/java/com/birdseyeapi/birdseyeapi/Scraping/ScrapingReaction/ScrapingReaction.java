package com.birdseyeapi.birdseyeapi.Scraping.ScrapingReaction;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.birdseyeapi.birdseyeapi.NewsReaction;

public interface ScrapingReaction {
    public abstract String getSourceBy();

    public abstract List<NewsReaction> extractReactions(String url, String title)
            throws InterruptedException, MalformedURLException;
}
