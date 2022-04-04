package com.birdseyeapi.birdseyeapi;

import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
    @OneToMany(mappedBy = "news", cascade = CascadeType.REMOVE)
    List<NewsReaction> reactions;
}
