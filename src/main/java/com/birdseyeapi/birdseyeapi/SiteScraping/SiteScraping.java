package com.birdseyeapi.birdseyeapi.SiteScraping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.birdseyeapi.birdseyeapi.News;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SiteScraping {
    private static final Logger LOG = LogManager.getLogger();
    
    public static List<News> scrape() {
        List<ScrapingBase> scrapingList = Arrays.asList(
            new ScrapeAtMarkIt(),
            new ScrapeCloudWatchImpress(),
            new ScrapeHatena(),
            new ScrapeZenn(),
            new ScrapeSrad(),
            new ScrapeZDNet(),
            new ScrapeGigazine()
        );
        List<News> newsList = new ArrayList<>();
        for (ScrapingBase scraping : scrapingList) {
            try {
                List<News> list = scraping.extractNews();
                list = list.subList(0, 5);
                LOG.info(scraping.getSourceBy() + " / scraped article: " + list.size());
                newsList.addAll(list);
            }
            catch (Exception e) {
                LOG.info(scraping.getSourceBy() + " / scraped failed...");
                LOG.info(e.getStackTrace());
            }
        }
        return newsList;
    }
}
