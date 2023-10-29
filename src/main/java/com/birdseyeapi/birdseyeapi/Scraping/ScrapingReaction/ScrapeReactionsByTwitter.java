package com.birdseyeapi.birdseyeapi.Scraping.ScrapingReaction;

import lombok.RequiredArgsConstructor;
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
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

import com.birdseyeapi.birdseyeapi.News.NewsReaction;

@Slf4j
@Component
@RequiredArgsConstructor
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
                    "article div[data-testid=\"tweetText\"]"));
            log.info("articles.size(): " + articles.size());
            for (WebElement article : articles) {
                String text = article.getText();
                if (text == null || text.trim().isEmpty() || text.equals(title)) {
                    continue;
                }
                log.info("-------------------------");
                log.info(text);
                NewsReaction reaction = new NewsReaction();
                reaction.setAuthor("twitter user");
                reaction.setComment(text);
                reaction.setScrapedDateTime(ZonedDateTime.now(ZoneId.of("UTC")));
                reaction.setCommentUrl(url);
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
