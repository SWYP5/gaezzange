package com.swyp.gaezzange.api.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentForm {
    private String userId;
    private String nickname;
    private String profileImagePath;
    private String commentContent;
    private Long commentLikeCount;
}
