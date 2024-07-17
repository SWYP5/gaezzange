package com.swyp.gaezzange.api.feed.dto.feed;

import com.swyp.gaezzange.domain.category.Category;
import com.swyp.gaezzange.domain.tendency.Tendency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedForm {
    private Tendency tendency;
    private Category category;
    private String content;
    private String feedImage;
}
