package com.swyp.gaezzange.api.feed.dto.feed;

import com.swyp.gaezzange.domain.category.FeedCategory;
import com.swyp.gaezzange.domain.tendency.Tendency;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedDetailDto {
    private Long userId;
    private String nickname;
    private String profileImagePath;
    private String feedContent;
    private String feedImagePath;
    private Long likeCount;
    private Long commentCount;
    private Tendency feedTendency;
    private FeedCategory category;
    private Boolean isLike;
    private LocalDateTime createdAt;
}
