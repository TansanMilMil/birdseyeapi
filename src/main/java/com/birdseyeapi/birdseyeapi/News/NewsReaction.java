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
    private long id;
    @Column(length = 200)
    private String author;
    @Column(columnDefinition = "TEXT")
    private String comment;
    private ZonedDateTime scrapedDateTime;
    @ManyToOne
    private News news;
    @Column(columnDefinition = "TEXT")
    private String commentUrl;
}
