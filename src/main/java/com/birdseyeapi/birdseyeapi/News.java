package com.birdseyeapi.birdseyeapi;

public class News {
    public long id;
    public String title;
    public String description;
    public String sourceBy;

    public News(long id, String title, String description, String sourceBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.sourceBy = sourceBy;
    }
}
