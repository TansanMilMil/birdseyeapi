package com.birdseyeapi.birdseyeapi.ScrapingNews;

import java.io.IOException;
import java.util.List;

import com.birdseyeapi.birdseyeapi.News;

public interface ScrapingBase {
    public abstract String getSourceBy();
    public abstract List<News> extractNews() throws IOException;
}
