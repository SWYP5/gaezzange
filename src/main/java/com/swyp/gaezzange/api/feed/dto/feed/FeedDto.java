package com.swyp.gaezzange.api.feed.dto.feed;

import com.swyp.gaezzange.api.feed.dto.comment.CommentForm;
import com.swyp.gaezzange.domain.category.Category;
import com.swyp.gaezzange.domain.tendency.Tendency;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedDto {
  private String userId;
  private String nickname;
  private Tendency tendency;
  private Category category;
  private String profileImagePath;
  private String feedContent;
  private String feedImagePath;
}
