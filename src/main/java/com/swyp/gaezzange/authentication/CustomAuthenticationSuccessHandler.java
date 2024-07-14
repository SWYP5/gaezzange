package com.swyp.gaezzange.authentication;

import com.swyp.gaezzange.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JWTUtil jwtUtil;

    @Value("${jwt.refreshTokenExpirationTime}")
    private Long refreshTokenExpirationTime;

    @Value("${auth.success.redirectUrl}")
    private String redirectUrl;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String accessToken = createJwtToken("access" ,authentication, customUserDetails);
        String refreshToken = createJwtToken("refresh" ,authentication, customUserDetails);

        response.addCookie(createCookie("refreshToken", refreshToken, (int) (refreshTokenExpirationTime / 1000)));
        response.setHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(redirectUrl);
    }

    private Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        return cookie;
    }

    private String createJwtToken(String category, Authentication authentication, CustomOAuth2User customUserDetails) {
        List<String> roleNames = customUserDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String email = authentication.getName();
        String provider = customUserDetails.getProvider();
        Long authUserId = customUserDetails.getUserAuthId();
        String role = roleNames.isEmpty() ? null : roleNames.get(0);

        return jwtUtil.createJwt(category, authUserId, email, role, provider, refreshTokenExpirationTime);
    }


}
