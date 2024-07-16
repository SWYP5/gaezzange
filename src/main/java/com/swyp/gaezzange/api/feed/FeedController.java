package com.swyp.gaezzange.api.feed;

import com.swyp.gaezzange.api.feed.dto.FeedForm;
import com.swyp.gaezzange.api.user.dto.UserBasicInfoDto;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FeedController", description = "Feed API")
@RestController("/v1/feed")
public class FeedController {

    @GetMapping("/{feedId}")
    public ApiResponse<FeedForm> getDetailByFeedId(@PathVariable Long feedId) {
        return ApiResponse.success(new FeedForm());
    }

}
