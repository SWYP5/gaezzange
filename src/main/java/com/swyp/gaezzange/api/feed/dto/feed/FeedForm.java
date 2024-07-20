package com.swyp.gaezzange.api.feed.dto.feed;

import com.swyp.gaezzange.domain.category.FeedCategory;
import com.swyp.gaezzange.domain.tendency.Tendency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedForm {
    private Tendency tendency;
    private FeedCategory category;
    private String content;
//    private MultipartFile feedImage;
}
