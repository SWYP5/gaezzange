package com.swyp.gaezzange.domain.feed.application;

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
import com.swyp.gaezzange.exception.customException.InvalidFileException;
import com.swyp.gaezzange.util.S3.FileStorage;
import com.swyp.gaezzange.util.S3.FileType;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

  public List<FeedDto> listFeeds(FeedSearchDto feedSearchDto) {
    List<Feed> feeds = feedService.getAllFeed(feedSearchDto);
    List<User> users = userService.findAllByUserIds(feeds.stream().map(Feed::getUserId).collect(Collectors.toList()));
    List<FeedImage> feedImages = feedImageService.findAllByFeedIds(feeds.stream().map(Feed::getFeedId).collect(Collectors.toList()));

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
              feed.getUserId().toString(),
              user.getNickname(),
              feed.getTendency(),
              feed.getCategory(),
              user.getProfileImagePath(),
              feed.getContent(),
              feedImage != null ? feedImage.getFeedImagePath() : null,
              feedLikeService.countByFeedId(feed.getFeedId()),
              commentService.commentCountByFeedId(feed.getFeedId())
          );
        })
        .toList();
  }

  public FeedDetailDto getFeed(Long feedId) {
    Feed feed = feedService.getFeed(feedId);
    User user = userService.getById(feed.getUserId())
        .orElseThrow(() -> new RuntimeException("not found user"));

    long commentCount = commentService.commentCountByFeedId(feedId);
    long feedLikeCount = feedLikeService.countByFeedId(feedId);

    return FeedDetailDto.builder()
      .nickname(user.getNickname())
      .feedContent(feed.getContent())
      .feedImagePath(user.getProfileImagePath())
      .likeCount(feedLikeCount)
      .commentCount(commentCount)
      .build();
  }

  @Transactional
  public void addFeed(long userId, FeedForm feedForm, MultipartFile feedImageFile) {
    Feed savedFeed = feedService.registerFeed(userId, feedForm);
    try {
      String imagePath = fileStorage.upload(FileType.FEED_IMAGE, feedImageFile);
      FeedImage feedImage = FeedImage.builder()
          .feedId(savedFeed.getFeedId())
          .feedImagePath(imagePath)
          .deleted(false)
          .build();
      feedImageService.saveFeedImage(feedImage);
    } catch (InvalidFileException e) {
      throw new BizException("NOT_VALID_IMAGE", "이미지 파일만 업로드할 수 있습니다.");
    } catch (Exception e) {
      log.error("[FAILED_TO_REGISTER_FEED] userId: {}", userId, e);
      throw new BizException("FAILED_TO_REGISTER_FEED", e.getMessage());
    }
  }

  @Transactional
  public void updateFeed(long userId, Long feedId, FeedForm feedForm, MultipartFile feedImageFile) {
    Feed feed = feedService.getFeed(feedId);

    if (!feed.validateUserId(userId)) {
      throw new BizException("PERMISSION_DENIED", "권한이 없습니다.");
    }

    Feed updateFeed = feedService.updateFeed(feed, feedForm);
    FeedImage feedImage = feedImageService.getFeedImageById(feedId)
        .orElse(null);

    if (feedImageFile != null && !feedImageFile.isEmpty()) {
      try {
        if (feedImage != null) {
          // 기존 이미지를 업데이트
          String newImagePath = fileStorage.updateFile(FileType.FEED_IMAGE, feedImage.getFeedImagePath(), feedImageFile);
          feedImage.updateFeedImagePath(newImagePath);
        } else {
          // 새 이미지를 삽입
          String newImagePath = fileStorage.upload(FileType.FEED_IMAGE, feedImageFile);
          feedImage = FeedImage.builder()
              .feedId(feedId)
              .feedImagePath(newImagePath)
              .deleted(false)
              .build();
        }
        feedImageService.saveFeedImage(feedImage);
      } catch (InvalidFileException e) {
        throw new InvalidFileException("이미지 파일만 업로드할 수 있습니다.");
      } catch (Exception e) {
        throw new BizException("FILE_UPDATE_FAILED", "파일 업데이트에 실패했습니다.");
      }
    }
  }

  @Transactional
  public void removeFeed(long userId, Long feedId) {
    Optional<FeedImage> feedImage = feedImageService.getFeedImageById(feedId);

    Feed feed = feedService.getFeed(feedId);

    if (!feed.validateUserId(userId)) {
      throw new BizException("PERMISSION_DENIED", "권한이 없습니다.");
    }

    feedService.deleteFeed(feed);

    if (feedImage.isPresent()) {
      try {
        fileStorage.deleteFile(feedImage.get().getFeedImagePath());
      } catch (Exception e) {
        throw new BizException("FILE_DELETE_FAILED", "파일 삭제에 실패했습니다.");
      }
    }
  }

  public void toggleLike(long userId, Long feedId) {
    feedLikeService.toggleLike(userId, feedId);
  }
}
