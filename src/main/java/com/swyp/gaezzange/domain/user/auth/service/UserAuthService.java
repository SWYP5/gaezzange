package com.swyp.gaezzange.domain.user.auth.service;

import com.swyp.gaezzange.domain.user.auth.repository.UserAuth;
import com.swyp.gaezzange.domain.user.auth.repository.UserAuthRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthService {
  private final UserAuthRepository userAuthRepository;

  public Optional<UserAuth> getById(Long userAuthId) {
    return userAuthRepository.findById(userAuthId);
  }

  public Optional<UserAuth> getByProviderCode(String providerCode) {
    return userAuthRepository.findByProviderCode(providerCode);
  }

  @Transactional
  public UserAuth saveUserAuth(UserAuth userAuth) {
    return userAuthRepository.save(userAuth);
  }
}
