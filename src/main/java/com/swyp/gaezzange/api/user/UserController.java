package com.swyp.gaezzange.api.user;

import com.swyp.gaezzange.api.user.dto.UserBasicInfoDto;
import com.swyp.gaezzange.api.user.dto.UserInfoForm;
import com.swyp.gaezzange.api.user.dto.UserProfileForm;
import com.swyp.gaezzange.authentication.UserContextProvider;
import com.swyp.gaezzange.domain.user.UserApplication;
import com.swyp.gaezzange.domain.user.auth.repository.UserAuth;
import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.exception.customException.BizException;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserController", description = "User API")
@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

  private final UserApplication userApplication;
  private final UserContextProvider userContextProvider;

  @GetMapping("/my-info")
  public ApiResponse<UserBasicInfoDto> getUser() {
    User user = userContextProvider.getUser();

    return ApiResponse.success(UserBasicInfoDto.from(user));
  }

  @PostMapping("/onboarding")
  public ApiResponse onboard(@Valid @RequestBody UserInfoForm form) {
    UserAuth userAuth = userContextProvider.getUserAuth();
    if (userContextProvider.getUser() != null) {
      throw new BizException("ALREADY_ONBOARDED", "이미 온보딩을 완료했습니다.");
    }

    userApplication.onboard(userAuth, form);
    return ApiResponse.success(null);
  }

  @PutMapping("/profile")
  public ApiResponse updateUserProfile(@Valid @RequestBody UserProfileForm form) {
    User user = userContextProvider.getUser();

    userApplication.updateProfile(user, form);
    return ApiResponse.success(null);
  }
}

