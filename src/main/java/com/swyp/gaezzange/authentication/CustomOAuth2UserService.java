package com.swyp.gaezzange.authentication;

import com.swyp.gaezzange.domain.user.auth.repository.UserAuth;
import com.swyp.gaezzange.domain.user.auth.repository.UserAuthRepository;
import com.swyp.gaezzange.domain.user.role.UserRole;
import com.swyp.gaezzange.util.OAuth2Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserAuthRepository userAuthRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    OAuth2Response oAuth2Response = getOAuth2Response(oAuth2User, registrationId);

    if (oAuth2Response == null) {
      return null;
    }

    String providerCode = oAuth2Response.getProviderId();
    UserAuth userAuth = userAuthRepository.findByProviderCode(providerCode)
        .orElseGet(() -> createUserAuth(oAuth2Response));

    return new CustomOAuth2User(userAuth);
  }

  private UserAuth createUserAuth(OAuth2Response response) {
    UserAuth createdUserAuth = UserAuth.createUserAuth(response.getEmail(), response.getProvider(),
        UserRole.USER, response.getProviderId());
    return userAuthRepository.save(createdUserAuth);
  }


  private OAuth2Response getOAuth2Response(OAuth2User oAuth2User, String registrationId) {
    if ("kakao".equals(registrationId)) {
      return new KaKaoResponse(oAuth2User.getAttributes());
    } else if ("google".equals(registrationId)) {
      return new GoogleResponse(oAuth2User.getAttributes());
    }
    return null;
  }
}
