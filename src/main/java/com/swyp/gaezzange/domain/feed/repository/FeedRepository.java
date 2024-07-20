package com.swyp.gaezzange.domain.feed.repository;

import com.swyp.gaezzange.domain.category.FeedCategory;
import com.swyp.gaezzange.domain.tendency.Tendency;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedRepository extends JpaRepository<Feed, Long> {
  @Query("SELECT f FROM Feed f WHERE (:tendency IS NULL OR f.tendency = :tendency) " +
      "AND (:category IS NULL OR f.category = :category) " +
      "AND (:searchText IS NULL OR f.content LIKE %:searchText%) " +
      "AND f.deleted = false")
  List<Feed> searchFeeds(@Param("tendency") Tendency tendency,
      @Param("category") FeedCategory category,
      @Param("searchText") String searchText);
}
