package com.swyp.gaezzange.service.feed;

import com.swyp.gaezzange.api.feed.dto.feed.FeedForm;
import com.swyp.gaezzange.domain.feed.repository.Feed;
import com.swyp.gaezzange.domain.feed.repository.FeedRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

  private final FeedRepository feedRepository;

  public void registerFeed(long userId, FeedForm feedForm) {
    Feed feed = Feed.builder()
        .userId(userId)
        .tendency(feedForm.getTendency())
        .category(feedForm.getCategory())
        .content(feedForm.getContent())
        .build();
    feedRepository.save(feed);
  }

  public void updateFeed(long userId, String feedId, FeedForm feedForm) {
    Feed feed = getOptionalFeed(feedId)
        .orElseThrow(() -> new RuntimeException("Feed not found"));

    if (!feed.validateUserId(userId)) {
      throw new RuntimeException("User not authorized to update feed");
    }

    feed.updateFeed(feedForm.getTendency(), feedForm.getCategory(), feedForm.getContent());
    feedRepository.save(feed);
  }

  public void deleteFeed(long userId, String feedId) {
    Feed feed = getOptionalFeed(feedId)
        .orElseThrow(() -> new RuntimeException("Feed not found"));

    if (!feed.validateUserId(userId)) {
      throw new RuntimeException("User not authorized to delete feed");
    }

    feed.deleteFeed();
    feedRepository.save(feed);
  }

  private Optional<Feed> getOptionalFeed(String feedId) {
    return feedRepository.findById(Long.valueOf(feedId));
  }
}
