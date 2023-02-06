package com.birdseyeapi.birdseyeapi;

import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    public long scrapingUnitId;
    @Column(length = 200)
    public String title;
    @Column(columnDefinition = "TEXT")
    public String description;
    public String sourceBy;
    @Column(columnDefinition = "TEXT")
    public String scrapedUrl;
    public ZonedDateTime scrapedDateTime;
    @Column(columnDefinition = "TEXT")
    public String articleUrl;
    @Column(columnDefinition = "TEXT")
    public String articleImageUrl;
    @OneToMany(mappedBy = "news")
    List<NewsReaction> reactions;
}
