package com.swyp.gaezzange.domain.feed.like.feed.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
  Optional<FeedLike> findByFeedIdAndUserId(Long feedId, Long userId);

  @Query("SELECT fl FROM FeedLike fl WHERE fl.feedId IN :feedIds")
  List<FeedLike> findAllByFeedIds(List<Long> feedIds);

  long countByFeedId(Long feedId);
}
