package com.birdseyeapi.birdseyeapi.SiteScraping;

import com.birdseyeapi.birdseyeapi.NewsReaction;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ScrapeReactionsByTwitter {
    private static final Logger LOG = LogManager.getLogger();
    
    public static List<NewsReaction> extractReactions(String url, String title) throws InterruptedException, MalformedURLException {
        List<NewsReaction> reactions = new ArrayList<>();
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        
        DesiredCapabilities firefox = DesiredCapabilities.firefox();
        WebDriver driver = new RemoteWebDriver(new URL(System.getenv("SELENIUM_URL")), firefox);
        LOG.info("selenium is ready.");
        driver.get("https://twitter.com/search?f=tweets&vertical=default&q=" + url);
        LOG.info("selenium is requesting twitter.");
        Thread.sleep(1000);
        LOG.info("request completed.");
        
        List<WebElement> articles = driver.findElements(By.cssSelector("#react-root > div > div > div > main > div > div > div > div > div > div:nth-child(2) > div > section > div > div > div > div > div > article > div > div > div > div > div > div:nth-child(2) > div:nth-child(1)"));
        LOG.info("articles.size(): " + articles.size());
        for (WebElement article : articles) {
            String text = article.getText();
            if (text == null || text.trim().isEmpty() || text.equals(title)) {
                continue;
            }
            LOG.info("-------------------------");
            LOG.info(text);
            NewsReaction reaction = new NewsReaction();
            reaction.author = "twitter user";
            reaction.comment = text;
            reaction.scrapedDateTime = now;
            reactions.add(reaction);
        }
        driver.quit();
        LOG.info("selenium quit.");
        return reactions;
    }
}
