package com.birdseyeapi.birdseyeapi;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewsWithReactionCount {
    private long id;
    private String title;
    private String description;
    private String sourceBy;
    private String scrapedUrl;
    private ZonedDateTime scrapedDateTime;
    private String articleUrl;
    private String articleImageUrl;   
    private long reactionCount;
}
