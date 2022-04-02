package com.birdseyeapi.birdseyeapi;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class NewsReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    @Column(length = 200)
    public String author;
    @Column(columnDefinition = "TEXT")
    public String comment;
    public ZonedDateTime scrapedDateTime;
    @ManyToOne
    public News news;
}
