package com.swyp.gaezzange.domain.feed.comment;

import com.swyp.gaezzange.api.feed.dto.comment.CommentDto;
import com.swyp.gaezzange.api.feed.dto.comment.CommentForm;
import com.swyp.gaezzange.domain.feed.comment.service.CommentService;
import com.swyp.gaezzange.domain.feed.like.comment.service.CommentLikeService;
import com.swyp.gaezzange.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentApplication {

  private final CommentService commentService;
  private final UserService userService;
  private final CommentLikeService commentLikeService;

  public CommentDto getComment(Long commentId) {
//    Comment comment = commentService.getComment(commentId)
//        .orElseThrow();
    return null;
  }

  public void addComment(long userId, Long feedId, CommentForm commentForm) {
    commentService.registerComment(userId, feedId, commentForm);
  }

  public void updateComment(long userId, Long commentId, CommentForm commentForm) {
    commentService.updateComment(userId, commentId, commentForm);
  }

  public void removeComment(long userId, Long commentId) {
    commentService.deleteComment(userId, commentId);
  }

  public void toggleLike(long userId, Long commentId) {
    commentLikeService.toggleLike(userId, commentId);
  }

}
