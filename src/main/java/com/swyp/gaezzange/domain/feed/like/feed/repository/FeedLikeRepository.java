package com.swyp.gaezzange.domain.feed.like.feed.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
  Optional<FeedLike> findByFeedIdAndUserId(Long feedId, Long userId);
}
