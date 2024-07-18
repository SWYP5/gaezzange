package com.swyp.gaezzange.authentication;

import com.swyp.gaezzange.domain.user.auth.repository.AuthToken;
import com.swyp.gaezzange.domain.user.auth.repository.AuthTokenRepository;
import com.swyp.gaezzange.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JWTUtil jwtUtil;

    @Value("${jwt.refreshTokenExpirationTime}")
    private Long refreshTokenExpirationTime;

    private final AuthTokenRepository authTokenRepository;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        String accessToken = createJwtToken("access" ,authentication, customUserDetails);
        String refreshToken = createJwtToken("refresh" ,authentication, customUserDetails);

        response.addCookie(createCookie("refreshToken", refreshToken, (int) (refreshTokenExpirationTime / 1000)));
        response.setHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + accessToken);

        HttpSession session = request.getSession();
        session.setAttribute("accessToken", accessToken);

        AuthToken authToken = AuthToken.builder()
                .userAuthId(customUserDetails.getUserAuthId())
                .expiresAt(LocalDateTime.now().plusSeconds(10))
                .token(refreshToken)
                .build();

        AuthToken savedAuthToken = authTokenRepository.save(authToken);

        String referer = Optional.ofNullable(request.getHeader("Referer"))
            .orElse("http://localhost:3000");
        if(!referer.endsWith("/")) {
            referer = referer + "/";
        }
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(referer + "token?tokenKey=" + savedAuthToken.getTokenId());
        log.info("Login success: {}", customUserDetails.getUserAuthId());
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
        Long userAuthId = customUserDetails.getUserAuthId();
        Long userId = customUserDetails.getUserId();
        String role = roleNames.isEmpty() ? null : roleNames.get(0);

        return jwtUtil.createJwt(category, userAuthId, userId, email, role, provider, refreshTokenExpirationTime);
    }


}
