package com.birdseyeapi.birdseyeapi.Scraping.ScrapingReaction;

import com.birdseyeapi.birdseyeapi.NewsReaction;

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
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

@Slf4j
public class ScrapeReactionsByTwitter implements ScrapingReaction {
    private final String SOURCE_BY = "twitter";
    private final String SOURCE_URL = "https://twitter.com/search?src=typed_query&f=liv&q=";

    @Override
    public String getSourceBy() {
        return SOURCE_BY;
    }

    @Override
    public List<NewsReaction> extractReactions(String url, String title)
            throws InterruptedException, MalformedURLException {
        List<NewsReaction> reactions = new ArrayList<>();

        FirefoxOptions browserOptions = new FirefoxOptions();
        WebDriver driver = new RemoteWebDriver(new URL(System.getenv("SELENIUM_URL")), browserOptions);
        try {
            log.info("selenium is ready.");
            url = SOURCE_URL + url;
            driver.get(url);
            log.info("selenium is requesting twitter.");
            Thread.sleep(5000);
            log.info("request completed.");

            List<WebElement> articles = driver.findElements(By.cssSelector(
                    "#react-root > div > div > div > main > div > div > div > div > div > div:nth-child(3) > div > section > div > div > div > div > div > article"));
            log.info("articles.size(): " + articles.size());
            for (WebElement article : articles) {
                String text = article.getText();
                if (text == null || text.trim().isEmpty() || text.equals(title)) {
                    continue;
                }
                log.info("-------------------------");
                log.info(text);
                NewsReaction reaction = new NewsReaction();
                reaction.author = "twitter user";
                reaction.comment = text;
                reaction.scrapedDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
                reaction.commentUrl = url;
                reactions.add(reaction);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            driver.quit();
            log.info("selenium quit.");
        }
        return reactions;

    }
}
