package com.birdseyeapi.birdseyeapi.News;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsReactionRepository extends JpaRepository<NewsReaction, Integer> {
    @Query("""
        SELECT MAX(n.scrapingUnitId)
        FROM News n
    """)
    Long findMaxScrapingUnitId();

    @Query("""
        SELECT
            NEW com.birdseyeapi.birdseyeapi.News.NewsWithReactionCount(
                n.id
                , MAX(n.title)
                , MAX(n.description)
                , MAX(n.summarizedText)
                , MAX(n.sourceBy)
                , MAX(n.scrapedUrl)
                , MAX(n.scrapedDateTime)
                , MAX(n.articleUrl)
                , MAX(n.articleImageUrl)
                , COUNT(r.id)
            )
        FROM News n
        LEFT JOIN n.reactions r
        WHERE n.scrapingUnitId >= :maxScrapingUnitId
        GROUP BY n.id
    """)
    List<NewsWithReactionCount> selectNewsWithReactionCount(@Param("maxScrapingUnitId") long maxScrapingUnitId);

    @Query("""
        SELECT n
        FROM News n
        LEFT JOIN FETCH n.reactions
        WHERE n.id = :id
    """)
    List<News> selectNewsReactionsById(@Param("id") long id);

    @Query("""
        SELECT n
        FROM News n
        LEFT JOIN FETCH n.reactions
        WHERE n.scrapingUnitId = :scrapingUnitId
    """)
    List<News> selectNewsReactionsByScrapingUnitId(@Param("scrapingUnitId") long scrapingUnitId);
}
