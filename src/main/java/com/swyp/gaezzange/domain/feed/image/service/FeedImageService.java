package com.swyp.gaezzange.domain.feed.image.service;

import com.swyp.gaezzange.domain.feed.image.repository.FeedImage;
import com.swyp.gaezzange.domain.feed.image.repository.FeedImageRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedImageService {

  private final FeedImageRepository feedImageRepository;

  public void saveFeedImage(FeedImage feedImage) {
    feedImageRepository.save(feedImage);
  }

  public Optional<FeedImage> getFeedImageById(Long feedId) {
    return feedImageRepository.findByFeedIdAndDeletedFalse(feedId);
  }

  public void deleteFeedImage(Long feedId) {
    feedImageRepository.deleteById(feedId);
  }

  public List<FeedImage> findAllByFeedIds(List<Long> feedIds) {
    return feedImageRepository.findAllByFeedIds(feedIds);
  }

}
