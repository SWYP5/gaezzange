package com.swyp.gaezzange.api.feed.dto.comment;

import com.swyp.gaezzange.domain.feed.comment.repository.Comment;
import com.swyp.gaezzange.domain.user.repository.User;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentDto {
  private Long userId;
  private Long parentCommentId;
  private Long commentId;
  private String nickname;
  private String profileImagePath;
  private String commentContent;
  private LocalDateTime commentCreatedAt;
  private Long commentLikeCount;
  private Boolean isCommentLike;

  public static CommentDto from(Comment comment, User user, long commentLikeCount, boolean isCommentLike) {
    return CommentDto.builder()
        .commentId(comment.getCommentId())
        .parentCommentId(comment.getParentComment() == null ? null : comment.getParentComment().getCommentId())
        .commentCreatedAt(comment.getCreatedAt())
        .userId(comment.getUserId())
        .nickname(user.getNickname())
        .profileImagePath(user.getProfileImagePathWithS3Url())
        .commentContent(comment.getContent())
        .commentLikeCount(commentLikeCount)
        .isCommentLike(isCommentLike)
        .build();
  }
}
