package com.birdseyeapi.birdseyeapi.ScrapingReaction;

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
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ScrapeReactionsByHatena {
    private static final Logger LOG = LogManager.getLogger();
    
    public static List<NewsReaction> extractReactions(String url, String title) throws InterruptedException, MalformedURLException {
        List<NewsReaction> reactions = new ArrayList<>();
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        
        DesiredCapabilities firefox = DesiredCapabilities.firefox();
        WebDriver driver = new RemoteWebDriver(new URL(System.getenv("SELENIUM_URL")), firefox);
        LOG.info("selenium is ready.");
        url = url.replace("http://", "");
        url = url.replace("https://", "");
        driver.get("https://b.hatena.ne.jp/entry/s/" + url);
        LOG.info("selenium is requesting hatena.");
        Thread.sleep(1000);
        LOG.info("request completed.");
        
        List<WebElement> articles = driver.findElements(By.cssSelector("#container > div > div.entry-contents > div.entry-main > div.entry-comments > div > div.bookmarks-sort-panels.js-bookmarks-sort-panels > div.is-active.bookmarks-sort-panel.js-bookmarks-sort-panel > div > div > div.entry-comment-contents-main > span.entry-comment-text.js-bookmark-comment"));
        LOG.info("articles.size(): " + articles.size());
        for (WebElement article : articles) {
            String text = article.getText();
            if (text == null || text.trim().isEmpty() || text.equals(title)) {
                continue;
            }
            LOG.info("-------------------------");
            LOG.info(text);
            NewsReaction reaction = new NewsReaction();
            reaction.author = "hatena user";
            reaction.comment = text;
            reaction.scrapedDateTime = now;
            reactions.add(reaction);
        }
        driver.quit();
        LOG.info("selenium quit.");
        return reactions;
    }
}
