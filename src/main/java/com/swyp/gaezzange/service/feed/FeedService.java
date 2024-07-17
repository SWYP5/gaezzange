package com.swyp.gaezzange.service.feed;

import com.swyp.gaezzange.api.feed.dto.feed.FeedDetailDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedForm;
import com.swyp.gaezzange.api.feed.dto.feed.FeedSearchDto;
import com.swyp.gaezzange.domain.feed.comment.repository.CommentRepository;
import com.swyp.gaezzange.domain.feed.like.feed.repository.FeedLikeRepository;
import com.swyp.gaezzange.domain.feed.repository.Feed;
import com.swyp.gaezzange.domain.feed.repository.FeedRepository;
import com.swyp.gaezzange.domain.user.repository.UserRepository;
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
        feed.getUserId().toString(), // Assuming userId is of type Long
        getNicknameByUserId(feed.getUserId()), // Implement this method to get nickname
        feed.getTendency(),
        feed.getCategory(),
        getProfileImagePathByUserId(feed.getUserId()), // Implement this method to get profile image path
        feed.getContent(),
        getFeedImagePathByFeedId(feed.getFeedId()) // Implement this method to get feed image path
    )).collect(Collectors.toList());
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

  private String getNicknameByUserId(Long userId) {
    // Implement this method to fetch the user's nickname based on userId
    return "nickname"; // Placeholder
  }

  private String getProfileImagePathByUserId(Long userId) {
    // Implement this method to fetch the user's profile image path based on userId
    return "profileImagePath"; // Placeholder
  }

  private String getFeedImagePathByFeedId(Long feedId) {
    // Implement this method to fetch the feed image path based on feedId
    return "feedImagePath"; // Placeholder
  }
}
