package com.birdseyeapi.birdseyeapi.News;

import java.time.ZonedDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
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
    @Column(columnDefinition = "TEXT")
    public String commentUrl;
}
