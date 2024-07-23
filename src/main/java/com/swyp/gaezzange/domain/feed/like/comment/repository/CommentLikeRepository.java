package com.swyp.gaezzange.domain.feed.like.comment.repository;

import com.swyp.gaezzange.domain.feed.like.feed.repository.FeedLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
  Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);
  Optional<CommentLike> findOneByCommentIdAndUserIdAndDeletedIsFalse(Long commentId, Long userId);
  long countByCommentIdAndDeletedIsFalse(Long commentId);
}
