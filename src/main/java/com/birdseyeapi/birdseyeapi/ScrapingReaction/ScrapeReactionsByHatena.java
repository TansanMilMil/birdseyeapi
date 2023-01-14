package com.birdseyeapi.birdseyeapi.ScrapingReaction;

import com.birdseyeapi.birdseyeapi.NewsReaction;
import com.birdseyeapi.birdseyeapi.ScrapingNews.ScrapingNews;

import lombok.extern.slf4j.Slf4j;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

@Slf4j
public class ScrapeReactionsByHatena implements ScrapingReaction {
    private final String SOURCE_BY = "hatena";
    private final String SOURCE_URL = "https://b.hatena.ne.jp/entry/s/";

    @Override
    public String getSourceBy() {
        return SOURCE_BY;
    }

    @Override
    public List<NewsReaction> extractReactions(String url, String title)
            throws InterruptedException, MalformedURLException {
        List<NewsReaction> reactions = new ArrayList<>();
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));

        DesiredCapabilities firefox = DesiredCapabilities.firefox();
        WebDriver driver = new RemoteWebDriver(new URL(System.getenv("SELENIUM_URL")), firefox);
        try {
            log.info("selenium is ready.");
            url = url.replace("http://", "");
            url = url.replace("https://", "");
            url = SOURCE_URL + url;
            driver.get(url);
            log.info("selenium is requesting hatena.");
            Thread.sleep(1000);
            log.info("request completed.");

            List<WebElement> articles = driver.findElements(By.cssSelector(
                    "#container > div > div.entry-contents > div.entry-main > div.entry-comments > div > div.bookmarks-sort-panels.js-bookmarks-sort-panels > div.is-active.bookmarks-sort-panel.js-bookmarks-sort-panel > div > div > div.entry-comment-contents-main > span.entry-comment-text.js-bookmark-comment"));
            log.info("articles.size(): " + articles.size());
            for (WebElement article : articles) {
                String text = article.getText();
                if (text == null || text.trim().isEmpty() || text.equals(title)) {
                    continue;
                }
                log.info("-------------------------");
                log.info(text);
                NewsReaction reaction = new NewsReaction();
                reaction.author = "hatena user";
                reaction.comment = text;
                reaction.scrapedDateTime = now;
                reaction.commentUrl = url;
                reactions.add(reaction);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error(e.getStackTrace().toString());
        } finally {
            driver.quit();
            log.info("selenium quit.");
        }
        return reactions;
    }
}
