package com.swyp.gaezzange.domain.feed.image.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedImageRepository extends JpaRepository<FeedImage, Long> {

  Optional<FeedImage> findByFeedIdAndDeletedFalse(long feedId);
}
