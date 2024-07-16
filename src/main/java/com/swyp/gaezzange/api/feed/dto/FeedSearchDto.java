package com.swyp.gaezzange.api.feed.dto;

import com.swyp.gaezzange.domain.category.Category;
import com.swyp.gaezzange.domain.tendency.Tendency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedSearchDto {
    private Tendency tendency;
    private Category category;
    private String searchText;
}
