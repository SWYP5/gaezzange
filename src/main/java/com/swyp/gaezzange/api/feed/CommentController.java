package com.swyp.gaezzange.api.feed;

import com.swyp.gaezzange.api.feed.dto.comment.CommentForm;
import com.swyp.gaezzange.domain.user.auth.repository.UserAuth;
import com.swyp.gaezzange.service.feed.CommentLikeService;
import com.swyp.gaezzange.service.feed.CommentService;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CommentController", description = "Comment API")
@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final CommentLikeService commentLikeService;


  @GetMapping("/v1/feed/{feedId}/{commentId}")
  public ApiResponse<String> getDetailByFeedId(@PathVariable Long commentId) {
    return ApiResponse.success(null);
  }

  @PostMapping("/v1/feed/{feedId}/comment")
  public ApiResponse<String> registerComment(@AuthenticationPrincipal UserAuth userAuth, @PathVariable String feedId, @RequestBody CommentForm commentForm) {
    long testId = (long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    commentService.registerComment(testId, feedId, commentForm);
    return ApiResponse.success(null);
  }

//  @PostMapping("/v1/feed/{feedId}/{commentId}/comment")
//  public ApiResponse<String> registerCommentReply(@AuthenticationPrincipal UserAuth userAuth, @PathVariable String feedId, CommentForm commentForm) {
//    commentService.registerComment(userAuth.getUser().getUserId(), feedId, commentForm);
//    return ApiResponse.success(null);
//  }

  @PutMapping("/v1/feed/{feedId}/{commentId}")
  public ApiResponse<String> updateComment(@AuthenticationPrincipal UserAuth userAuth, @PathVariable String commentId, @RequestBody CommentForm commentForm) {
    long testId = (long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    commentService.updateComment(testId, commentId, commentForm);
    return ApiResponse.success(null);
  }

  @DeleteMapping("/v1/feed/{feedId}/{commentId}")
  public ApiResponse<String> deleteComment(@AuthenticationPrincipal UserAuth userAuth, @PathVariable String commentId) {
    long testId = (long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    commentService.deleteComment(testId, commentId);
    return ApiResponse.success(null);
  }

  @PostMapping("/v1/feed/{feedId}/{commentId}/like")
  public ApiResponse<String> likeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserAuth userAuth) {
    long testId = (long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    commentLikeService.like(testId, String.valueOf(commentId));
    return ApiResponse.success(null);
  }

  @DeleteMapping("/v1/feed/{feedId}/{commentId}/unlike")
  public ApiResponse<String> unlikeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserAuth userAuth) {
    long testId = (long) SecurityContextHolder.getContext().getAuthentication().getCredentials();
    commentLikeService.unlike(testId, String.valueOf(commentId));
    return ApiResponse.success(null);
  }

}
