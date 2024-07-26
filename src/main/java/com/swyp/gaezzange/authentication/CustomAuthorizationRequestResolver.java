package com.swyp.gaezzange.authentication;


import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

  private final OAuth2AuthorizationRequestResolver defaultResolver;

  public CustomAuthorizationRequestResolver(
      ClientRegistrationRepository clientRegistrationRepository) {
    this.defaultResolver = new DefaultOAuth2AuthorizationRequestResolver(
        clientRegistrationRepository, "/oauth2/authorization");
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
    OAuth2AuthorizationRequest authorizationRequest = this.defaultResolver.resolve(request);
    return authorizationRequest != null ? customAuthorizationRequest(request,
        authorizationRequest) : null;
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request,
      String clientRegistrationId) {
    OAuth2AuthorizationRequest authorizationRequest = this.defaultResolver.resolve(request,
        clientRegistrationId);
    return authorizationRequest != null ? customAuthorizationRequest(request,
        authorizationRequest) : null;
  }

  private OAuth2AuthorizationRequest customAuthorizationRequest(
      HttpServletRequest request,
      OAuth2AuthorizationRequest authorizationRequest) {
    String customState =
        request.getHeader("Referer") + "_" + UUID.randomUUID(); // Custom state 값 생성
    return OAuth2AuthorizationRequest.from(authorizationRequest)
        .state(customState)
        .build();
  }
}