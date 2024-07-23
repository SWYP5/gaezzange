package com.swyp.gaezzange.api.feed.dto.comment;

import com.swyp.gaezzange.api.routine.dto.RoutineDto;
import com.swyp.gaezzange.domain.feed.comment.repository.Comment;
import com.swyp.gaezzange.domain.user.repository.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommentDto {
  private Long userId;
  private String nickname;
  private String profileImagePath;
  private String commentContent;
  private Long commentLikeCount;

  public static CommentDto from(Comment comment, User user, long commentLikeCount) {
    return CommentDto.builder()
        .userId(comment.getUserId())
        .nickname(user.getNickname())
        .profileImagePath(user.getProfileImagePath())
        .commentContent(comment.getContent())
        .commentLikeCount(commentLikeCount)
        .build();
  }
}
