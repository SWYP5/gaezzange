package com.swyp.gaezzange.domain.feed.comment.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  long countByFeedIdAndParentCommentIsNullAndDeletedFalse(long feedId);
  List<Comment> findAllByFeedIdAndDeletedIsFalse(long feedId);
}
