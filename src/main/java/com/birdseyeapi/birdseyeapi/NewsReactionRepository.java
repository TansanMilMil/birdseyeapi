package com.birdseyeapi.birdseyeapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsReactionRepository extends JpaRepository<NewsReaction, Integer> {
}
