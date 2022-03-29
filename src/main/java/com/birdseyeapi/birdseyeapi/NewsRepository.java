package com.birdseyeapi.birdseyeapi;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    List<News> findByscrapedDateTimeGreaterThanEqual(ZonedDateTime scrapedDateTime);
}
