package com.swyp.gaezzange.domain.feed.image.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedImageRepository extends JpaRepository<FeedImage, Long> {

  Optional<FeedImage> findByFeedId(long feedId);
  Optional<FeedImage> findByFeedIdAndDeletedFalse(long feedId);

  @Query("SELECT fi FROM FeedImage fi WHERE fi.feedId IN :feedIds AND fi.deleted = false")
  List<FeedImage> findAllByFeedIds(@Param("feedIds")List<Long> feedIds);
}
