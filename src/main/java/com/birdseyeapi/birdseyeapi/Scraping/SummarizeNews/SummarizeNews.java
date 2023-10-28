package com.birdseyeapi.birdseyeapi.Scraping.SummarizeNews;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.birdseyeapi.birdseyeapi.AI.Summarize.AISummarizer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SummarizeNews {
    private final AISummarizer aiSummarizer;

    public String summarize(String articleUrl) throws IOException {
        if (!StringUtils.hasText(articleUrl)) {
            return null;
        }
        Document doc = Jsoup.connect(articleUrl).get();
        try {
            return aiSummarizer.summarize(doc.body().text());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
