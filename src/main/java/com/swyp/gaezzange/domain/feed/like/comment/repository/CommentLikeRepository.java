package com.swyp.gaezzange.domain.feed.like.comment.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
  Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);
}
