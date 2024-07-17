package com.swyp.gaezzange.api.feed;

import com.swyp.gaezzange.api.feed.dto.feed.FeedDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedForm;
import com.swyp.gaezzange.authentication.CustomOAuth2User;
import com.swyp.gaezzange.domain.user.auth.repository.UserAuth;
import com.swyp.gaezzange.service.feed.FeedLikeService;
import com.swyp.gaezzange.service.feed.FeedService;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FeedController", description = "Feed API")
@RestController
@RequestMapping(("/v1/feed"))
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final FeedLikeService feedLikeService;

    @GetMapping("/v1/feed/{feedId}")
    public ApiResponse<FeedDto> getDetailByFeedId(@PathVariable Long feedId) {
        return ApiResponse.success(new FeedDto());
    }

    @PostMapping("/v1/feed")
    public ApiResponse<FeedDto> registerFeed(@AuthenticationPrincipal CustomOAuth2User userAuth, @RequestBody FeedForm feedForm) {
        long testId = (long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        feedService.registerFeed(testId, feedForm);;
        return ApiResponse.success(null);
    }

    @PutMapping("/v1/feed/{feedId}")
    public ApiResponse<FeedDto> updateFeed(@AuthenticationPrincipal UserAuth userAuth, @PathVariable String feedId, @RequestBody FeedForm feedForm) {
        long testId = (long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        feedService.updateFeed(testId, feedId, feedForm);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/v1/feed/{feedId}")
    public ApiResponse<FeedDto> deleteFeed(@AuthenticationPrincipal UserAuth userAuth, @PathVariable String feedId) {
        long testId = (long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        feedService.deleteFeed(testId, feedId);
        return ApiResponse.success(null);
    }

    @PostMapping("/v1/feed/{feedId}/like")
    public ApiResponse<String> likeFeed(@PathVariable Long feedId, @AuthenticationPrincipal UserAuth userAuth) {
        long testId = (long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        feedLikeService.like(testId, String.valueOf(feedId));
        return ApiResponse.success(null);
    }

    @DeleteMapping("/v1/feed/{feedId}/unlike")
    public ApiResponse<String> unlikeFeed(@PathVariable Long feedId, @AuthenticationPrincipal UserAuth userAuth) {
        long testId = (long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        feedLikeService.unlike(testId, String.valueOf(feedId));
        return ApiResponse.success(null);
    }

}
