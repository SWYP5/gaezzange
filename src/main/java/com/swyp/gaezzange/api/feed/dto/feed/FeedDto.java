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
public class FeedDto {
  private Long userId;
  private Long feedId;
  private String nickname;
  private Tendency tendency;
  private FeedCategory category;
  private String profileImagePath;
  private String feedContent;
  private String feedImagePath;
  private LocalDateTime feedCreatedAt;
  private Boolean isLike;
  private Long feedLikeCount;
  private Long feedCommentCount;
}
