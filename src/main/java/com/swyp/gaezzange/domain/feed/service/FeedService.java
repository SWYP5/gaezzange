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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FeedService {

  private final FeedRepository feedRepository;
  private final FeedLikeRepository feedLikeRepository;
  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final FileStorage fileStorage;
  private final FeedImageRepository feedImageRepository;

  @Transactional
  public void registerFeed(long userId, FeedForm feedForm, MultipartFile feedImageFile) {
    Feed feed = Feed.builder()
        .userId(userId)
        .tendency(feedForm.getTendency())
        .category(feedForm.getCategory())
        .content(feedForm.getContent())
        .deleted(false)
        .build();
    Feed savedFeed = feedRepository.save(feed);
    try {
      String imagePath = fileStorage.uploadFile(FileType.FEED, feedImageFile);
      FeedImage feedImage = FeedImage.builder()
          .feedId(savedFeed.getFeedId())
          .feedImagePath(imagePath)
          .deleted(false)
          .build();
      feedImageRepository.save(feedImage);
    } catch (InvalidFileException e) {
      feedRepository.delete(savedFeed); // 이미지 업로드 실패 시 피드 삭제
      throw new InvalidFileException("이미지 파일만 업로드할 수 있습니다.");
    } catch (Exception e) {
      feedRepository.delete(savedFeed); // 이미지 업로드 실패 시 피드 삭제
    }
  }

  public FeedDetailDto getFeed(long feedId) {
    Feed feed = feedRepository.findById(feedId)
        .orElseThrow(() -> new RuntimeException("not found feed"));
    User user = userRepository.findById(feed.getUserId())
        .orElseThrow(() -> new RuntimeException("not found user"));

    long feedLikeCount = feedLikeRepository.countByFeedId(feedId);
    long commentCount = commentRepository.countByFeedIdAndParentCommentIsNullAndDeletedFalse(feedId);

    return FeedDetailDto.builder()
        .nickname(user.getNickname())
        .feedContent(feed.getContent())
        .feedImagePath(user.getProfileImagePath())
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

  @Transactional
  public void updateFeed(long userId, long feedId, FeedForm feedForm, MultipartFile feedImageFile) {
    Feed feed = getOptionalFeed(feedId)
        .orElseThrow(() -> new BizException("NOT_FOUND", "피드가 없습니다."));

    if (!feed.validateUserId(userId)) {
      throw new BizException("PERMISSION_DENIED", "권한이 없습니다.");
    }

    feed.updateFeed(feedForm.getTendency(), feedForm.getCategory(), feedForm.getContent());
    feedRepository.save(feed);

    FeedImage feedImage = feedImageRepository.findByFeedIdAndDeletedFalse(feedId)
        .orElse(null);

    if (feedImageFile != null && !feedImageFile.isEmpty()) {
      try {
        if (feedImage != null) {
          // 기존 이미지를 업데이트
          String newImagePath = fileStorage.updateFile(FileType.FEED, feedImage.getFeedImagePath(), feedImageFile);
          feedImage.updateFeedImagePath(newImagePath);
        } else {
          // 새 이미지를 삽입
          String newImagePath = fileStorage.uploadFile(FileType.FEED, feedImageFile);
          feedImage = FeedImage.builder()
              .feedId(feedId)
              .feedImagePath(newImagePath)
              .deleted(false)
              .build();
        }
        feedImageRepository.save(feedImage);
      } catch (InvalidFileException e) {
        throw new InvalidFileException("이미지 파일만 업로드할 수 있습니다.");
      } catch (Exception e) {
        throw new BizException("FILE_UPDATE_FAILED", "파일 업데이트에 실패했습니다.");
      }
    }

  }

  public void deleteFeed(long userId, long feedId) {
    Feed feed = getOptionalFeed(feedId)
        .orElseThrow(() -> new BizException("NOT_FOUND", "피드가 없습니다."));

    if (!feed.validateUserId(userId)) {
      throw new BizException("PERMISSION_DENIED", "권한이 없습니다.");
    }

    feed.deleteFeed();
    feedRepository.save(feed);

    FeedImage feedImage = feedImageRepository.findByFeedIdAndDeletedFalse(feedId)
        .orElseThrow(() -> new RuntimeException("not found feed image"));

    try {
      fileStorage.deleteFile(feedImage.getFeedImagePath());
      if (!fileStorage.checkFileDeleted(feedImage.getFeedImagePath())) {
        throw new BizException("DELETE_FAILED", "파일 삭제에 실패했습니다.");
      }
      feedImage.deleteImage();
      feedImageRepository.save(feedImage);
    } catch (Exception e) {
      throw new BizException("DELETE_FAILED", "삭제에 실패했습니다.");
    }

  }

  private String getNicknameByUserId(Long userId) {
    User user = getOptionalUser(userId)
        .orElseThrow(() -> new RuntimeException("not found user"));
    return user.getNickname();
  }

  private String getProfileImagePathByUserId(Long userId) {
    User user = getOptionalUser(userId)
        .orElseThrow(() -> new RuntimeException("not found user"));
    return user.getProfileImagePath();
  }

  private String getFeedImagePathByFeedId(Long feedId) {
    FeedImage feedImage = feedImageRepository.findByFeedIdAndDeletedFalse(feedId)
        .orElseThrow(() -> new RuntimeException("not found feed"));
    return feedImage.getFeedImagePath();
  }

  private Optional<Feed> getOptionalFeed(long feedId) {
    return feedRepository.findById(feedId);
  }

  private Optional<User> getOptionalUser(Long userId) {
    return userRepository.findById(userId);
  }
}
