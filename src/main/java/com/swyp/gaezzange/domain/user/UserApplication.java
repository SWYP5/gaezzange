package com.swyp.gaezzange.domain.user;

import com.swyp.gaezzange.api.user.dto.UserInfoForm;
import com.swyp.gaezzange.api.user.dto.UserProfileForm;
import com.swyp.gaezzange.domain.user.auth.repository.UserAuth;
import com.swyp.gaezzange.domain.user.auth.service.UserAuthService;
import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserApplication {

  private final UserService userService;
  private final UserAuthService userAuthService;

  @Transactional
  public User onboard(UserAuth userAuth, UserInfoForm form) {
    User user = userService.saveUser(User.from((form)));
    userAuthService.updateUser(userAuth, user);
    return user;
  }

  @Transactional
  public void updateProfile(User user, UserProfileForm form) {
    userService.saveUser(user.updateProfile((form)));
  }
}
