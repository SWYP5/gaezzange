package com.swyp.gaezzange.domain.feed.service;

import com.swyp.gaezzange.api.feed.dto.feed.FeedDetailDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedForm;
import com.swyp.gaezzange.api.feed.dto.feed.FeedSearchDto;
import com.swyp.gaezzange.domain.feed.comment.repository.CommentRepository;
import com.swyp.gaezzange.domain.feed.image.repository.FeedImage;
import com.swyp.gaezzange.domain.feed.image.repository.FeedImageRepository;
import com.swyp.gaezzange.domain.feed.like.feed.repository.FeedLikeRepository;
import com.swyp.gaezzange.domain.feed.repository.Feed;
import com.swyp.gaezzange.domain.feed.repository.FeedRepository;
import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.domain.user.repository.UserRepository;
import com.swyp.gaezzange.exception.customException.BizException;
import com.swyp.gaezzange.exception.customException.InvalidFileException;
import com.swyp.gaezzange.util.S3.FileStorage;
import com.swyp.gaezzange.util.S3.FileType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
