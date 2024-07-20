package com.swyp.gaezzange.api.feed;

import com.swyp.gaezzange.api.feed.dto.feed.FeedDetailDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedForm;
import com.swyp.gaezzange.api.feed.dto.feed.FeedSearchDto;
import com.swyp.gaezzange.authentication.CustomOAuth2User;
import com.swyp.gaezzange.authentication.UserContextProvider;
import com.swyp.gaezzange.domain.feed.application.FeedApplication;
import com.swyp.gaezzange.domain.user.auth.repository.UserAuth;
import com.swyp.gaezzange.domain.feed.like.feed.service.FeedLikeService;
import com.swyp.gaezzange.domain.feed.service.FeedService;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "FeedController", description = "Feed API")
@RestController
@RequestMapping(("/v1/feed"))
@RequiredArgsConstructor
public class FeedController {

    private final UserContextProvider userContextProvider;
    private final FeedApplication feedApplication;
//    private final FeedService feedService;
//    private final FeedLikeService feedLikeService;

    @GetMapping()
    public ApiResponse<List<FeedDto>> getFeeds(FeedSearchDto feedSearchDto) {
        return ApiResponse.success(feedApplication.listFeeds(feedSearchDto));
    }

    @GetMapping("/{feedId}")
    public ApiResponse<FeedDetailDto> getFeed(@PathVariable Long feedId) {
        return ApiResponse.success(feedApplication.getFeed(feedId));
    }

    @PostMapping()
    public ApiResponse<FeedDetailDto> addFeed(
        @RequestPart("feedForm") FeedForm feedForm,
        @RequestPart(value = "feedImage", required = false) MultipartFile feedImageFile
    )
    {
        feedApplication.addFeed(userContextProvider.getUserId(), feedForm, feedImageFile);;
        return ApiResponse.success(null);
    }

    @PutMapping("/{feedId}")
    public ApiResponse<FeedDetailDto> updateFeed(
        @PathVariable Long feedId,
        @RequestPart("feedForm") FeedForm feedForm,
        @RequestPart(value = "feedImage", required = false) MultipartFile feedImageFile
    )
    {
        feedApplication.updateFeed(userContextProvider.getUserId(), feedId, feedForm, feedImageFile);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{feedId}")
    public ApiResponse<FeedDetailDto> removeFeed(@PathVariable Long feedId) {
        feedApplication.removeFeed(userContextProvider.getUserId(), feedId);
        return ApiResponse.success(null);
    }

    @PostMapping("/{feedId}/like-toggle")
    public ApiResponse<String> likeFeed(@PathVariable Long feedId) {
        feedApplication.toggleLike(userContextProvider.getUserId(), feedId);
        return ApiResponse.success(null);
    }
}
