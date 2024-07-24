package com.swyp.gaezzange.domain.feed;

import static com.swyp.gaezzange.contants.ExceptionConstants.S3ExceptionConstants.CODE_FAILED_FILE_DELETE;
import static com.swyp.gaezzange.contants.ExceptionConstants.S3ExceptionConstants.MESSAGE_FAILED_FILE_DELETE;
import static com.swyp.gaezzange.contants.ExceptionConstants.UserExceptionConstants.CODE_USER_NOT_FOUND;
import static com.swyp.gaezzange.contants.ExceptionConstants.UserExceptionConstants.MESSAGE_USER_NOT_FOUND;
import static com.swyp.gaezzange.contants.SystemConstants.S3Constants.S3_URL;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

import com.swyp.gaezzange.api.feed.dto.feed.FeedDetailDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedForm;
import com.swyp.gaezzange.api.feed.dto.feed.FeedSearchDto;
import com.swyp.gaezzange.domain.feed.comment.service.CommentService;
import com.swyp.gaezzange.domain.feed.image.repository.FeedImage;
import com.swyp.gaezzange.domain.feed.image.service.FeedImageService;
import com.swyp.gaezzange.domain.feed.like.feed.service.FeedLikeService;
import com.swyp.gaezzange.domain.feed.repository.Feed;
import com.swyp.gaezzange.domain.feed.service.FeedService;
import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.domain.user.service.UserService;
import com.swyp.gaezzange.exception.customException.BizException;
import com.swyp.gaezzange.util.S3.FileStorage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedApplication {

  private final FeedService feedService;
  private final FeedLikeService feedLikeService;
  private final UserService userService;
  private final CommentService commentService;
  private final FeedImageService feedImageService;
  private final FileStorage fileStorage;

  public List<FeedDto> listFeeds(long userId, FeedSearchDto feedSearchDto) {
    List<Feed> feeds = feedService.getAllFeed(feedSearchDto);
    List<User> users = userService.findAllByUserIds(
        feeds.stream().map(Feed::getUserId).collect(Collectors.toList()));
    List<FeedImage> feedImages = feedImageService.findAllByFeedIds(
        feeds.stream().map(Feed::getFeedId).collect(Collectors.toList()));

    // userId와 User 매핑
    Map<Long, User> userMap = users.stream()
        .collect(Collectors.toMap(User::getUserId, user -> user));

    // feedId와 FeedImage 매핑
    Map<Long, FeedImage> feedImageMap = feedImages.stream()
        .collect(Collectors.toMap(FeedImage::getFeedId, feedImage -> feedImage));

    return feeds.stream()
        .map(feed -> {
          User user = userMap.get(feed.getUserId());
          FeedImage feedImage = feedImageMap.get(feed.getFeedId());

          return new FeedDto(
              feed.getUserId(),
              feed.getFeedId(),
              user.getNickname(),
              feed.getTendency(),
              feed.getCategory(),
              user.getProfileImagePath() != null ? S3_URL + user.getProfileImagePath() : null,
              feed.getContent(),
              feedImage != null ? S3_URL + feedImage.getFeedImagePath() : null,
              feed.getCreatedAt(),
              feedLikeService.existsLike(feed.getFeedId(), userId),
              feedLikeService.countByFeedId(feed.getFeedId()),
              commentService.commentCountByFeedId(feed.getFeedId())
          );
        })
        .toList();
  }

  public FeedDetailDto getFeed(Long feedId, Long userId) {
    Feed feed = feedService.getFeed(feedId);
    User user = userService.getById(feed.getUserId())
        .orElseThrow(() -> new BizException(CODE_USER_NOT_FOUND, MESSAGE_USER_NOT_FOUND));

    long commentCount = commentService.commentCountByFeedId(feedId);
    long feedLikeCount = feedLikeService.countByFeedId(feedId);
    boolean isLike = feedLikeService.existsLike(feedId, userId);

    return FeedDetailDto.builder()
        .nickname(user.getNickname())
        .feedContent(feed.getContent())
        .feedImagePath(S3_URL + user.getProfileImagePath())
        .likeCount(feedLikeCount)
        .commentCount(commentCount)
        .feedTendency(feed.getTendency())
        .category(feed.getCategory())
        .createdAt(feed.getCreatedAt())
        .isLike(isLike)
        .build();
  }

  @Transactional
  public void addFeed(long userId, FeedForm feedForm) {
    Feed savedFeed = feedService.registerFeed(userId, feedForm);
    FeedImage feedImage = FeedImage.builder()
        .feedId(savedFeed.getFeedId())
        .feedImagePath(feedForm.getFeedImagePath())
        .deleted(false)
        .build();
    feedImageService.saveFeedImage(feedImage);
  }

  @Transactional
  public void updateFeed(long userId, Long feedId, FeedForm feedForm) {
    Feed feed = feedService.getFeed(feedId);

    if (!feed.validateUserId(userId)) {
      throw new BizException("PERMISSION_DENIED", "권한이 없습니다.");
    }

    feedService.updateFeed(feed, feedForm);
    Optional<FeedImage> feedImage = feedImageService.getFeedImageById(feedId);

    String imagePath = feedForm.getFeedImagePath();
    if (isBlank(imagePath)) {
      feedImage.ifPresent(image -> removeFeedImageIfExist(image));
    } else {
      feedImage.ifPresentOrElse(
          image -> updateFeedImage(image, imagePath.replace(S3_URL, EMPTY)),
          () -> saveFeedImage(feedForm, feedId)
      );
    }
  }

  @Transactional
  public void removeFeed(long userId, Long feedId) {
    Optional<FeedImage> feedImage = feedImageService.getFeedImageById(feedId);

    Feed feed = feedService.getFeed(feedId);

    if (!feed.validateUserId(userId)) {
      throw new BizException("PERMISSION_DENIED", "권한이 없습니다.");
    }

    feedImage.ifPresent(image -> removeFeedImageIfExist(image));
    feedService.deleteFeed(feed);
  }

  private void removeFeedImageIfExist(FeedImage feedImage) {
    try {
      if (fileStorage.checkFileExists(feedImage.getFeedImagePath())) {
        fileStorage.deleteFile(feedImage.getFeedImagePath());
        feedImage.setDeleted(true);
      }
    } catch (Exception e) {
      throw new BizException(CODE_FAILED_FILE_DELETE, MESSAGE_FAILED_FILE_DELETE);
    }
  }


  private void updateFeedImage(FeedImage feedImage, String newFeedImagePath) {
    if (newFeedImagePath.equals(feedImage.getFeedImagePath())) {
      return;
    }

    if (!feedImage.isDeleted()) {
      removeFeedImageIfExist(feedImage);
    }
    feedImage.updateFeedImagePath(newFeedImagePath);
    feedImage.setDeleted(false);
  }

  private void saveFeedImage(FeedForm feedForm, long feedId) {
    feedImageService.saveFeedImage(
        FeedImage.builder()
            .feedId(feedId)
            .feedImagePath(feedForm.getFeedImagePath())
            .deleted(false)
            .build()
    );
  }

  @Transactional
  public void toggleLike(long userId, Long feedId) {
    feedLikeService.toggleLike(userId, feedId);
  }
}
