package com.birdseyeapi.birdseyeapi;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import com.birdseyeapi.birdseyeapi.AwsS3.S3Manager;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.model.S3Object;

@Service
public class NewsService {

    @Autowired
    private S3Manager s3Manager;
    private final String BUCKET_NAME = "birds-eye-news";

    public List<News> getTodayNews() throws IOException {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("UTC"));
        String prefix = now.format(DateTimeFormatter.ISO_LOCAL_DATE);
        List<S3Object> s3Objects = s3Manager.listObjects(BUCKET_NAME, prefix);

        List<News> newsList = new ArrayList<News>();
        for (ListIterator<S3Object> iterVals = s3Objects.listIterator(); iterVals.hasNext(); ) {
            S3Object s3Object = (S3Object) iterVals.next();
            ObjectMapper mapper = new ObjectMapper();

            String json = s3Manager.getJsonObject(BUCKET_NAME, s3Object.key());
            News news = mapper.readValue(json, News.class);
            newsList.add(news);
        }

        Collections.shuffle(newsList);

        return newsList;
    }
}
