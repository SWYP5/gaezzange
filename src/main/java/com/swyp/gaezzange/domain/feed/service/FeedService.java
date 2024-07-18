package com.swyp.gaezzange.domain.feed.service;

import com.swyp.gaezzange.api.feed.dto.feed.FeedDetailDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedForm;
import com.swyp.gaezzange.api.feed.dto.feed.FeedSearchDto;
import com.swyp.gaezzange.domain.feed.comment.repository.CommentRepository;
import com.swyp.gaezzange.domain.feed.like.feed.repository.FeedLikeRepository;
import com.swyp.gaezzange.domain.feed.repository.Feed;
import com.swyp.gaezzange.domain.feed.repository.FeedRepository;
import com.swyp.gaezzange.domain.user.repository.UserRepository;
import com.swyp.gaezzange.exception.customException.BizException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

  private final FeedRepository feedRepository;
  private final FeedLikeRepository feedLikeRepository;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;

  public void registerFeed(long userId, FeedForm feedForm) {
    System.out.println(feedForm.toString());
    Feed feed = Feed.builder()
        .userId(userId)
        .tendency(feedForm.getTendency())
        .category(feedForm.getCategory())
        .content(feedForm.getContent())
        .deleted(false)
        .build();
    feedRepository.save(feed);
  }

  public FeedDetailDto getFeed(long feedId) {
    Feed feed = feedRepository.findById(feedId)
        .orElseThrow(() -> new RuntimeException("not found feed"));

    long feedLikeCount = feedLikeRepository.countByFeedId(feedId);
    long commentCount = commentRepository.countByFeedIdAndParentCommentIsNullAndDeletedFalse(feedId);

    return FeedDetailDto.builder()
        .nickname("user x test")
        .feedContent(feed.getContent())
        .feedImagePath("user x test")
        .likeCount(feedLikeCount)
        .commentCount(commentCount)
        .build();
  }

  public List<FeedDto> getAllFeed(FeedSearchDto feedSearchDto) {
    List<Feed> feeds = feedRepository.searchFeeds(
        feedSearchDto.getTendency(),
        feedSearchDto.getCategory(),
        feedSearchDto.getSearchText()
    );

    return feeds.stream().map(feed -> new FeedDto(
        feed.getUserId().toString(),
        getNicknameByUserId(feed.getUserId()),
        feed.getTendency(),
        feed.getCategory(),
        getProfileImagePathByUserId(feed.getUserId()),
        feed.getContent(),
        getFeedImagePathByFeedId(feed.getFeedId())
    )).collect(Collectors.toList());
  }

  public void updateFeed(long userId, long feedId, FeedForm feedForm) {
    Feed feed = getOptionalFeed(feedId)
        .orElseThrow(() -> new BizException("NOT_FOUND", "피드가 없습니다."));

    if (!feed.validateUserId(userId)) {
      throw new BizException("PERMISSION_DENIED", "권한이 없습니다.");
    }

    feed.updateFeed(feedForm.getTendency(), feedForm.getCategory(), feedForm.getContent());
    feedRepository.save(feed);
  }

  public void deleteFeed(long userId, long feedId) {
    Feed feed = getOptionalFeed(feedId)
        .orElseThrow(() -> new BizException("NOT_FOUND", "피드가 없습니다."));

    if (!feed.validateUserId(userId)) {
      throw new BizException("PERMISSION_DENIED", "권한이 없습니다.");
    }

    feed.deleteFeed();
    feedRepository.save(feed);
  }

  private Optional<Feed> getOptionalFeed(long feedId) {
    return feedRepository.findById(feedId);
  }

  private String getNicknameByUserId(Long userId) {
    return "nickname"; // Placeholder
  }

  private String getProfileImagePathByUserId(Long userId) {
    return "profileImagePath"; // Placeholder
  }

  private String getFeedImagePathByFeedId(Long feedId) {
    return "feedImagePath"; // Placeholder
  }
}
