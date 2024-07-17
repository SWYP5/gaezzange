package com.swyp.gaezzange.service.feed;

import com.swyp.gaezzange.domain.feed.like.comment.repository.CommentLike;
import com.swyp.gaezzange.domain.feed.like.comment.repository.CommentLikeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLikeService extends AbstractLikeService<CommentLike>{

  private final CommentLikeRepository commentLikeRepository;

  @Override
  protected Optional<CommentLike> findLike(Long commentId, Long userId) {
    return commentLikeRepository.findByCommentIdAndUserId(commentId, userId);
  }

  @Override
  protected CommentLike createLike(Long commentId, Long userId) {
    return CommentLike.builder()
        .commentId(commentId)
        .userId(userId)
        .deleted(false)
        .build();
  }

  @Override
  protected void saveLike(CommentLike like) {
    commentLikeRepository.save(like);
  }

  @Override
  protected void toggleLike(CommentLike like) {
    like.toggleLike();
  }
}
