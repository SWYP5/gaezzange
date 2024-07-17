package com.swyp.gaezzange.service.feed;

import com.swyp.gaezzange.api.feed.dto.comment.CommentForm;
import com.swyp.gaezzange.domain.feed.comment.repository.Comment;
import com.swyp.gaezzange.domain.feed.comment.repository.CommentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;

  public void registerComment(long userId, String feedId, CommentForm commentForm) {
    Comment parentComment = getOptionalComment(commentForm.getCommentParentId())
        .orElse(null);
    Comment comment = Comment.builder()
        .userId(userId)
        .parentComment(parentComment)
        .feedId(Long.valueOf(feedId))
        .content(commentForm.getCommentContent())
        .build();
    commentRepository.save(comment);
  }

  public void updateComment(long userId, String commentId, CommentForm commentForm) {
    Comment comment = getOptionalComment(commentId)
        .orElseThrow(() -> new RuntimeException("Comment not found"));

    if (!comment.validateUserId(userId)) {
      throw new RuntimeException("User not authorized to update comment");
    }

    comment.updateContent(commentForm.getCommentContent());
    commentRepository.save(comment);
  }

  public void deleteComment(long userId, String commentId) {
    Comment comment = getOptionalComment(commentId)
        .orElseThrow(() -> new RuntimeException("Comment not found"));

    if (!comment.validateUserId(userId)) {
      throw new RuntimeException("User not authorized to delete comment");
    }

    comment.deleteComment();
    commentRepository.save(comment);
  }

  private Optional<Comment> getOptionalComment(String commentId) {
    return commentRepository.findById(Long.valueOf(commentId));
  }
}
