package com.swyp.gaezzange.domain.feed.like.feed.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
  Optional<FeedLike> findByFeedIdAndUserIdAndDeletedIsFalse(Long feedId, Long userId);

  @Query("SELECT fl FROM FeedLike fl WHERE fl.feedId IN :feedIds AND fl.deleted = false ")
  List<FeedLike> findAllByFeedIds(List<Long> feedIds);

  Optional<FeedLike> findOneByFeedIdAndUserIdAndDeletedIsFalse(Long feedId, Long userId);

  long countByFeedIdAndDeletedIsFalse(Long feedId);
}
