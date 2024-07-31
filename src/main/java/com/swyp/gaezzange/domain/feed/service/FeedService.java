package com.swyp.gaezzange.domain.feed.service;

import static com.swyp.gaezzange.contants.ExceptionConstants.FeedExceptionConstants.CODE_FEED_NOT_FOUND;
import static com.swyp.gaezzange.contants.ExceptionConstants.FeedExceptionConstants.MESSAGE_FEED_NOT_FOUND;

import com.swyp.gaezzange.api.feed.dto.feed.FeedForm;
import com.swyp.gaezzange.api.feed.dto.feed.FeedSearchDto;
import com.swyp.gaezzange.domain.feed.repository.Feed;
import com.swyp.gaezzange.domain.feed.repository.FeedRepository;
import com.swyp.gaezzange.exception.customException.BizException;
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
        .orElseThrow(() -> new BizException(CODE_FEED_NOT_FOUND, MESSAGE_FEED_NOT_FOUND));

  }

  public List<Feed> getAllFeed(FeedSearchDto feedSearchDto) {
    return feedRepository.searchFeeds(
        feedSearchDto.getTendency(),
        feedSearchDto.getCategory(),
        feedSearchDto.getSearchText()
    );
  }

  @Transactional
  public void updateFeed(Feed feed, FeedForm feedForm) {
    feed.updateFeed(feedForm.getTendency(), feedForm.getCategory(), feedForm.getContent());
  }

  @Transactional
  public void deleteFeed(Feed feed) {
    feed.deleteFeed();
    feedRepository.save(feed);
  }
}
