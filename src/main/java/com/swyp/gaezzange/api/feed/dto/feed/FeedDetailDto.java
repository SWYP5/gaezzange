package com.swyp.gaezzange.api.feed.dto.feed;

import com.swyp.gaezzange.api.feed.dto.comment.CommentForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedDetailDto {
    private String userId;
    private String nickname;
    private String profileImagePath;
    private String feedContent;
    private String feedImagePath;
    private Long likeCount;
    private Long commentCount;
    private List<CommentForm> commentForm;
}
