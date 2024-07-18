package com.swyp.gaezzange.domain.feed.like.service;

import java.util.Optional;

public abstract class AbstractLikeService<T> {

  protected abstract Optional<T> findLike(Long entityId, Long userId);
  protected abstract T createLike(Long entityId, Long userId);
  protected abstract void saveLike(T like);
  protected abstract void toggleLikeStatus(T like);

  public void toggleLike(Long userId, String entityId) {
    Long id = Long.valueOf(entityId);
    Optional<T> optionalLike = findLike(id, userId);

    if (optionalLike.isEmpty()) {
      T like = createLike(id, userId);
      saveLike(like);
    } else {
      T like = optionalLike.get();
      toggleLikeStatus(like);
      saveLike(like);
    }
  }
}
