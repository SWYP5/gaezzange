package com.swyp.gaezzange.authentication;

import static com.swyp.gaezzange.contants.ExceptionConstants.UserExceptionConstants.CODE_UNKNOWN_USER;
import static com.swyp.gaezzange.contants.ExceptionConstants.UserExceptionConstants.CODE_USER_NOT_FOUND;
import static com.swyp.gaezzange.contants.ExceptionConstants.UserExceptionConstants.MESSAGE_UNKNOWN_USER;
import static com.swyp.gaezzange.contants.ExceptionConstants.UserExceptionConstants.MESSAGE_USER_NOT_FOUND;

import com.swyp.gaezzange.domain.user.auth.repository.UserAuth;
import com.swyp.gaezzange.domain.user.auth.service.UserAuthService;
import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.exception.customException.BizException;
import com.swyp.gaezzange.util.TokenUserContext;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserContextProvider {

  private final UserAuthService userAuthService;

  public TokenUserContext getContext() {
    SecurityContext securityContext = SecurityContextHolder.getContext();
    return (TokenUserContext) securityContext.getAuthentication().getCredentials();
  }

  public UserAuth getUserAuth() {
    return userAuthService.getById(getContext().getUserAuthId())
        .orElseThrow(() -> new BizException(CODE_UNKNOWN_USER, MESSAGE_UNKNOWN_USER));
  }

  public User getUser() {
    return userAuthService.getById(getContext().getUserAuthId())
        .map(UserAuth::getUser)
        .orElseThrow(() -> new BizException(CODE_USER_NOT_FOUND, MESSAGE_USER_NOT_FOUND));
  }

  public long getUserId() {
    return Optional.ofNullable(getContext().getUserId())
        .orElseThrow(() -> new BizException(CODE_USER_NOT_FOUND, MESSAGE_USER_NOT_FOUND));
  }

  public Optional<Long> getUserIdOptional() {
    return Optional.ofNullable(getContext().getUserId());
  }
}
