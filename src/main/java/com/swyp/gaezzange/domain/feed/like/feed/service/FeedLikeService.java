package com.swyp.gaezzange.domain.feed.like.feed.service;

import com.swyp.gaezzange.domain.feed.like.feed.repository.FeedLike;
import com.swyp.gaezzange.domain.feed.like.feed.repository.FeedLikeRepository;
import com.swyp.gaezzange.domain.feed.like.service.AbstractLikeService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedLikeService extends AbstractLikeService<FeedLike> {

  private final FeedLikeRepository feedLikeRepository;

  @Override
  protected Optional<FeedLike> findLike(Long feedId, Long userId) {
    return feedLikeRepository.findByFeedIdAndUserId(feedId, userId);
  }

  @Override
  protected FeedLike createLike(Long feedId, Long userId) {
    return FeedLike.builder()
        .feedId(feedId)
        .userId(userId)
        .deleted(false)
        .build();
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
