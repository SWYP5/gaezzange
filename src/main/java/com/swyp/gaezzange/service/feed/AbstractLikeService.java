package com.swyp.gaezzange.service.feed;

import java.util.Optional;

public abstract class AbstractLikeService<T> {

  protected abstract Optional<T> findLike(Long entityId, Long userId);
  protected abstract T createLike(Long entityId, Long userId);
  protected abstract void saveLike(T like);
  protected abstract void toggleLike(T like);

  public void like(Long userId, String entityId) {
    Long id = Long.valueOf(entityId);
    Optional<T> optionalLike = findLike(id, userId);

    if (optionalLike.isEmpty()) {
      T like = createLike(id, userId);
      saveLike(like);
    } else {
      T like = optionalLike.get();
      toggleLike(like);
      saveLike(like);
    }
  }

  public void unlike(Long userId, String entityId) {
    Long id = Long.valueOf(entityId);
    T like = findLike(id, userId)
        .orElseThrow(() -> new RuntimeException("Like not found"));
    toggleLike(like);
    saveLike(like);
  }
}
