package com.swyp.gaezzange.api.feed;

import com.swyp.gaezzange.api.feed.dto.feed.FeedDetailDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedForm;
import com.swyp.gaezzange.api.feed.dto.feed.FeedSearchDto;
import com.swyp.gaezzange.authentication.UserContextProvider;
import com.swyp.gaezzange.domain.feed.FeedApplication;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FeedController", description = "Feed API")
@RestController
@RequestMapping(("/v1/feed"))
@RequiredArgsConstructor
public class FeedController {

    private final UserContextProvider userContextProvider;
    private final FeedApplication feedApplication;

    @GetMapping()
    public ApiResponse<List<FeedDto>> getFeeds(FeedSearchDto feedSearchDto) {
        return ApiResponse.success(feedApplication.listFeeds(userContextProvider.getUserId(), feedSearchDto));
    }

    @GetMapping("/{feedId}")
    public ApiResponse<FeedDetailDto> getFeed(@PathVariable Long feedId) {
        return ApiResponse.success(feedApplication.getFeed(feedId, userContextProvider.getUserId()));
    }

    @PostMapping()
    public ApiResponse addFeed(@RequestBody FeedForm feedForm)
    {
        feedApplication.addFeed(userContextProvider.getUserId(), feedForm);;
        return ApiResponse.success(null);
    }

    @PutMapping("/{feedId}")
    public ApiResponse updateFeed(
        @PathVariable Long feedId, @RequestBody FeedForm feedForm
    )
    {
        feedApplication.updateFeed(userContextProvider.getUserId(), feedId, feedForm);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{feedId}")
    public ApiResponse removeFeed(@PathVariable Long feedId) {
        feedApplication.removeFeed(userContextProvider.getUserId(), feedId);
        return ApiResponse.success(null);
    }

    @PostMapping("/{feedId}/like-toggle")
    public ApiResponse likeFeed(@PathVariable Long feedId) {
        feedApplication.toggleLike(userContextProvider.getUserId(), feedId);
        return ApiResponse.success(null);
    }
}
