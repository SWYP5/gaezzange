package com.swyp.gaezzange.domain.feed.like.feed.service;

import com.swyp.gaezzange.domain.feed.like.feed.repository.FeedLike;
import com.swyp.gaezzange.domain.feed.like.feed.repository.FeedLikeRepository;
import com.swyp.gaezzange.domain.feed.like.service.AbstractLikeService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedLikeService extends AbstractLikeService<FeedLike> {

  private final FeedLikeRepository feedLikeRepository;

  @Override
  protected Optional<FeedLike> findLike(Long feedId, Long userId) {
    return feedLikeRepository.findByFeedIdAndUserIdAndDeletedIsFalse(feedId, userId);
  }

  @Override
  protected FeedLike createLike(Long feedId, Long userId) {
    return FeedLike.builder()
        .feedId(feedId)
        .userId(userId)
        .deleted(false)
        .build();
  }

  public List<FeedLike> findAllByFeedIds(List<Long> feedIds) {
    return feedLikeRepository.findAllByFeedIds(feedIds);
  }

  public long countByFeedId(Long feedId) {
    return feedLikeRepository.countByFeedIdAndDeletedIsFalse(feedId);
  }

  public boolean existsLike(Long feedId, Long userId) {
    return feedLikeRepository.findOneByFeedIdAndUserIdAndDeletedIsFalse(feedId, userId).isPresent();
  }

  @Override
  protected void saveLike(FeedLike like) {
    feedLikeRepository.save(like);
  }

  @Override
  protected void toggleLikeStatus(FeedLike like) {
    like.toggleLike();
  }

}
