package com.swyp.gaezzange.api.feed;

import com.swyp.gaezzange.api.feed.dto.feed.FeedDetailDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedDto;
import com.swyp.gaezzange.api.feed.dto.feed.FeedForm;
import com.swyp.gaezzange.api.feed.dto.feed.FeedSearchDto;
import com.swyp.gaezzange.authentication.CustomOAuth2User;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FeedController", description = "Feed API")
@RestController
@RequestMapping(("/v1/feed"))
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final FeedLikeService feedLikeService;

    @GetMapping()
    public ApiResponse<List<FeedDto>> getAllFeed(FeedSearchDto feedSearchDto) {
        return ApiResponse.success(feedService.getAllFeed(feedSearchDto));
    }

    @GetMapping("/{feedId}")
    public ApiResponse<FeedDetailDto> getDetailByFeedId(@PathVariable Long feedId) {
        return ApiResponse.success(feedService.getFeed(feedId));
    }

    @PostMapping()
    public ApiResponse<FeedDetailDto> registerFeed(@AuthenticationPrincipal CustomOAuth2User userAuth, @RequestBody FeedForm feedForm) {
        long testId = (long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        feedService.registerFeed(testId, feedForm);;
        return ApiResponse.success(null);
    }

    @PutMapping("/{feedId}")
    public ApiResponse<FeedDetailDto> updateFeed(@AuthenticationPrincipal UserAuth userAuth, @PathVariable String feedId, @RequestBody FeedForm feedForm) {
        long testId = (long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        feedService.updateFeed(testId, feedId, feedForm);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{feedId}")
    public ApiResponse<FeedDetailDto> deleteFeed(@AuthenticationPrincipal UserAuth userAuth, @PathVariable String feedId) {
        long testId = (long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        feedService.deleteFeed(testId, feedId);
        return ApiResponse.success(null);
    }

    @PostMapping("/{feedId}/like-toggle")
    public ApiResponse<String> likeFeed(@PathVariable Long feedId, @AuthenticationPrincipal UserAuth userAuth) {
        long testId = (long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        feedLikeService.toggleLike(testId, String.valueOf(feedId));
        return ApiResponse.success(null);
    }
}
