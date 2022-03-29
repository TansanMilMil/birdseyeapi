package com.birdseyeapi.birdseyeapi.SiteScraping;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.birdseyeapi.birdseyeapi.News;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SiteScraping {
    private static final Logger LOG = LogManager.getLogger();
    
    public static List<News> scrape() throws IOException {
        List<ScrapingBase> scrapingList = Arrays.asList(
            new ScrapeAtMarkIt(),
            new ScrapeCloudWatchImpress(),
            new ScrapeHatena(),
            new ScrapeZenn(),
            new ScrapeSrad(),
            new ScrapeZDNet(),
            new ScrapeGigazine()
        );
        List<News> newsList = new ArrayList<News>();
        for (ScrapingBase scraping : scrapingList) {
            try {
                List<News> list = scraping.extractNews();
                //putToS3(newsList, scraping.getSourceBy());
                LOG.info(scraping.getSourceBy() + " / scraped article: " + list.size());
                newsList.addAll(list);
            }
            catch (Exception e) {
                LOG.info(scraping.getSourceBy() + " / scraped failed...");
                LOG.info(e.getStackTrace().toString());
            }
        }
        return newsList;
    }
}
