package com.swyp.gaezzange.configuration;

import com.swyp.gaezzange.authentication.CustomAuthenticationFailureHandler;
import com.swyp.gaezzange.authentication.CustomAuthenticationSuccessHandler;
import com.swyp.gaezzange.authentication.CustomAuthorizationRequestResolver;
import com.swyp.gaezzange.authentication.CustomLogoutSuccessHandler;
import com.swyp.gaezzange.authentication.OAuth2UserAuthProvider;
import com.swyp.gaezzange.domain.user.UserApplication;
import com.swyp.gaezzange.domain.user.auth.repository.AuthTokenRepository;
import com.swyp.gaezzange.domain.user.auth.repository.UserAuthRepository;
import com.swyp.gaezzange.domain.user.auth.service.UserAuthService;
import com.swyp.gaezzange.jwt.JWTFilter;
import com.swyp.gaezzange.jwt.JWTUtil;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class GaezzangeSecurityConfig {

  private final OAuth2UserAuthProvider oAuth2UserAuthProvider;
  private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
  private final CustomAuthenticationFailureHandler authenticationFailureHandler;
  private final CustomLogoutSuccessHandler logoutSuccessHandler;
  private final JWTUtil jwtUtil;
  private final AuthTokenRepository authTokenRepository;
  private final UserAuthService userAuthService;
  private final UserApplication userApplication;
  private final ClientRegistrationRepository clientRegistrationRepository;

  @Value("${jwt.secretKey}")
  private String secretKey;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, UserAuthRepository userAuthRepository)
      throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
        .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
        })
        .addFilterBefore(
            new JWTFilter(jwtUtil, authTokenRepository, userAuthService, userApplication),
            OAuth2LoginAuthenticationFilter.class)
        .authorizeHttpRequests(registry ->
            registry.requestMatchers("/api/hello").permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/login/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
        ).oauth2Login(oauth2Login ->
            oauth2Login
                .authorizationEndpoint(authorizationEndpoint ->
                    authorizationEndpoint
                        .authorizationRequestResolver(customAuthorizationRequestResolver())
                )
                .userInfoEndpoint((userInfoEndpointConfig) ->
                    userInfoEndpointConfig.userService(oAuth2UserAuthProvider)
                )
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)

        )
        .logout(logout ->
            logout
                .logoutSuccessUrl("/login")
                .logoutSuccessHandler(logoutSuccessHandler)
        ).exceptionHandling(exceptionHandling ->
            exceptionHandling
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        );

    return http.build();
  }

  CorsConfigurationSource corsConfigurationSource() {
    return request -> {
      CorsConfiguration config = new CorsConfiguration();
      config.setAllowedHeaders(Collections.singletonList("*"));
      config.addExposedHeader("Authorization");
      config.addExposedHeader("RefreshToken");
      config.setAllowedMethods(Collections.singletonList("*"));
      config.setAllowedOriginPatterns(
          List.of("http://localhost:3000", "https://gaejjange.swygbro.com",
              "http://localdev.com:3000"));
      config.setAllowCredentials(true);

      return config;
    };

  }

  @Bean
  public OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver() {
    return new CustomAuthorizationRequestResolver(clientRegistrationRepository);
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    MacAlgorithm algorithm = MacAlgorithm.HS256;

    return NimbusJwtDecoder.withSecretKey(
            new SecretKeySpec(secretKey.getBytes(), algorithm.getName()))
        .macAlgorithm(algorithm)
        .build();
  }

  @Bean
  public SecretKey secretKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
  }
}
