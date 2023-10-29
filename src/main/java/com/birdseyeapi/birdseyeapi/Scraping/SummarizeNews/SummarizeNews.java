package com.birdseyeapi.birdseyeapi.Scraping.SummarizeNews;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.birdseyeapi.birdseyeapi.AI.Summarize.AISummarizer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SummarizeNews {
    private final AISummarizer aiSummarizer;

    public String summarize(String articleUrl) {
        if (!StringUtils.hasText(articleUrl)) {
            return null;
        }
        
        try {
            Document doc = Jsoup.connect(articleUrl).get();
            return aiSummarizer.summarize(doc.body().text());
        } catch (Exception e) {
            log.error("cannot summarize! -> articleUrl: " + articleUrl);
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
