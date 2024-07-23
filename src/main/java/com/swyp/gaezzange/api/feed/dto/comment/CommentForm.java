package com.swyp.gaezzange.api.feed.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentForm {
    private Long commentParentId;
    private String commentContent;
}
