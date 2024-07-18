package com.swyp.gaezzange.api.feed;

import com.swyp.gaezzange.api.feed.dto.comment.CommentForm;
import com.swyp.gaezzange.authentication.UserContextProvider;
import com.swyp.gaezzange.domain.user.auth.repository.UserAuth;
import com.swyp.gaezzange.domain.feed.like.comment.service.CommentLikeService;
import com.swyp.gaezzange.domain.feed.comment.service.CommentService;
import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.exception.customException.BizException;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
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

  private final UserContextProvider userContextProvider;
  private final CommentService commentService;
  private final CommentLikeService commentLikeService;


  @GetMapping("/v1/feed/{feedId}/{commentId}")
  public ApiResponse<String> getDetailByFeedId(@PathVariable Long commentId) {
    return ApiResponse.success(null);
  }

  @PostMapping("/v1/feed/{feedId}/comment")
  public ApiResponse<String> registerComment(@PathVariable Long feedId, @RequestBody CommentForm commentForm) {
    commentService.registerComment(userContextProvider.getUserId(), feedId, commentForm);
    return ApiResponse.success(null);
  }

//  @PostMapping("/v1/feed/{feedId}/{commentId}/comment")
//  public ApiResponse<String> registerCommentReply(@AuthenticationPrincipal UserAuth userAuth, @PathVariable String feedId, CommentForm commentForm) {
//    commentService.registerComment(userAuth.getUser().getUserId(), feedId, commentForm);
//    return ApiResponse.success(null);
//  }

  @PutMapping("/v1/feed/{feedId}/{commentId}")
  public ApiResponse<String> updateComment(@AuthenticationPrincipal UserAuth userAuth, @PathVariable Long commentId, @RequestBody CommentForm commentForm) {
    commentService.updateComment(userContextProvider.getUserId(), commentId, commentForm);
    return ApiResponse.success(null);
  }

  @DeleteMapping("/v1/feed/{feedId}/{commentId}")
  public ApiResponse<String> deleteComment(@AuthenticationPrincipal UserAuth userAuth, @PathVariable Long commentId) {
    commentService.deleteComment(userContextProvider.getUserId(), commentId);
    return ApiResponse.success(null);
  }

  @PostMapping("/v1/feed/{feedId}/{commentId}/like-toggle")
  public ApiResponse<String> likeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserAuth userAuth) {
    commentLikeService.toggleLike(userContextProvider.getUserId(), commentId);
    return ApiResponse.success(null);
  }
}
