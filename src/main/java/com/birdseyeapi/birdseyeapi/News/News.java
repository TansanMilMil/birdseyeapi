package com.birdseyeapi.birdseyeapi.News;

import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long scrapingUnitId;
    @Column(length = 200)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "TEXT")
    private String summarizedText;
    private String sourceBy;
    @Column(columnDefinition = "TEXT")
    private String scrapedUrl;
    private ZonedDateTime scrapedDateTime;
    @Column(columnDefinition = "TEXT")
    private String articleUrl;
    @Column(columnDefinition = "TEXT")
    private String articleImageUrl;
    @OneToMany(mappedBy = "news")
    List<NewsReaction> reactions;
}
