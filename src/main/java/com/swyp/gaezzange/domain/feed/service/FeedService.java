package com.swyp.gaezzange.domain.feed.service;

import com.swyp.gaezzange.api.feed.dto.feed.FeedForm;
import com.swyp.gaezzange.api.feed.dto.feed.FeedSearchDto;
import com.swyp.gaezzange.domain.feed.repository.Feed;
import com.swyp.gaezzange.domain.feed.repository.FeedRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FeedService {

  private final FeedRepository feedRepository;

  @Transactional
  public Feed registerFeed(long userId, FeedForm feedForm) {
    Feed feed = Feed.builder()
        .userId(userId)
        .tendency(feedForm.getTendency())
        .category(feedForm.getCategory())
        .content(feedForm.getContent())
        .deleted(false)
        .build();
    return feedRepository.save(feed);
  }

  public Feed getFeed(long feedId) {
    return feedRepository.findById(feedId)
        .orElseThrow(() -> new RuntimeException("not found feed"));
  }

  public List<Feed> getAllFeed(FeedSearchDto feedSearchDto) {
    return feedRepository.searchFeeds(
        feedSearchDto.getTendency(),
        feedSearchDto.getCategory(),
        feedSearchDto.getSearchText()
    );
  }

  @Transactional
  public Feed updateFeed(Feed feed, FeedForm feedForm) {
    feed.updateFeed(feedForm.getTendency(), feedForm.getCategory(), feedForm.getContent());
    return feedRepository.save(feed);
  }

  @Transactional
  public void deleteFeed(Feed feed) {
    feedRepository.save(feed);
  }
}
