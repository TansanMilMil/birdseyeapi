package com.birdseyeapi.birdseyeapi;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
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
}
