package com.swyp.gaezzange.authentication;

import com.swyp.gaezzange.domain.user.auth.repository.UserAuth;
import com.swyp.gaezzange.domain.user.auth.service.UserAuthService;
import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.exception.customException.BizException;
import com.swyp.gaezzange.util.TokenUserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserContextProvider {

  private final UserAuthService userAuthService;

  public TokenUserContext getContext() {
    return (TokenUserContext) SecurityContextHolder.getContext().getAuthentication().getDetails();
  }

  public UserAuth getUserAuth() {
    return userAuthService.getById(getContext().getUserAuthId())
        .orElseThrow(() -> new BizException("UNKNOWN_USER", "다시 로그인 해주세요."));
  }

  public User getUser() {
    return userAuthService.getById(getContext().getUserAuthId())
        .map(userAuth -> userAuth.getUser())
        .orElse(null);
  }
}
