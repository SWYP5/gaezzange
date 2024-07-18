package com.swyp.gaezzange.domain.feed.comment.service;

import com.swyp.gaezzange.api.feed.dto.comment.CommentForm;
import com.swyp.gaezzange.domain.feed.comment.repository.Comment;
import com.swyp.gaezzange.domain.feed.comment.repository.CommentRepository;
import com.swyp.gaezzange.exception.customException.BizException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;

  public void registerComment(long userId, Long feedId, CommentForm commentForm) {
    Comment parentComment = getOptionalComment(Long.valueOf(commentForm.getCommentParentId()))
        .orElse(null);
    Comment comment = Comment.builder()
        .userId(userId)
        .parentComment(parentComment)
        .feedId(feedId)
        .content(commentForm.getCommentContent())
        .build();
    commentRepository.save(comment);
  }

  public void updateComment(long userId, Long commentId, CommentForm commentForm) {
    Comment comment = getOptionalComment(commentId)
        .orElseThrow(() -> new BizException("NOT_FOUND", "코멘트가 없습니다."));

    if (!comment.validateUserId(userId)) {
      throw new BizException("PERMISSION_DENIED", "권한이 없습니다.");
    }

    comment.updateContent(commentForm.getCommentContent());
    commentRepository.save(comment);
  }

  public void deleteComment(long userId, Long commentId) {
    Comment comment = getOptionalComment(commentId)
        .orElseThrow(() -> new BizException("NOT_FOUND", "코멘트가 없습니다."));

    if (!comment.validateUserId(userId)) {
      throw new BizException("PERMISSION_DENIED", "권한이 없습니다.");
    }

    comment.deleteComment();
    commentRepository.save(comment);
  }

  private Optional<Comment> getOptionalComment(Long commentId) {
    if (commentId == null) {
      return Optional.empty();
    } else {
      return commentRepository.findById(commentId);
    }
  }
}