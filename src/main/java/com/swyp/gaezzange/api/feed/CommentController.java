package com.swyp.gaezzange.api.feed;

import com.swyp.gaezzange.api.feed.dto.comment.CommentForm;
import com.swyp.gaezzange.authentication.UserContextProvider;
import com.swyp.gaezzange.domain.feed.comment.CommentApplication;
import com.swyp.gaezzange.domain.user.auth.repository.UserAuth;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "CommentController", description = "Comment API")
@RequestMapping(("/v1/feed"))
@RestController
@RequiredArgsConstructor
public class CommentController {

  private final UserContextProvider userContextProvider;
  private final CommentApplication commentApplication;


  @GetMapping("/{feedId}/{commentId}")
  public ApiResponse<String> getComment(@PathVariable Long commentId) {
    return ApiResponse.success(commentApplication.getComment(commentId));
  }

  @PostMapping("/{feedId}/comment")
  public ApiResponse<String> addComment(@PathVariable Long feedId, @RequestBody CommentForm commentForm) {
    commentApplication.addComment(userContextProvider.getUserId(), feedId, commentForm);
    return ApiResponse.success(null);
  }

  @PutMapping("/{feedId}/{commentId}")
  public ApiResponse<String> updateComment(@AuthenticationPrincipal UserAuth userAuth, @PathVariable Long commentId, @RequestBody CommentForm commentForm) {
    commentApplication.updateComment(userContextProvider.getUserId(), commentId, commentForm);
    return ApiResponse.success(null);
  }

  @DeleteMapping("/{feedId}/{commentId}")
  public ApiResponse<String> deleteComment(@AuthenticationPrincipal UserAuth userAuth, @PathVariable Long commentId) {
    commentApplication.removeComment(userContextProvider.getUserId(), commentId);
    return ApiResponse.success(null);
  }

  @PostMapping("/{feedId}/{commentId}/like-toggle")
  public ApiResponse<String> likeComment(@PathVariable Long commentId, @AuthenticationPrincipal UserAuth userAuth) {
    commentApplication.toggleLike(userContextProvider.getUserId(), commentId);
    return ApiResponse.success(null);
  }
}
