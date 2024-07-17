package com.swyp.gaezzange.jwt;

import com.swyp.gaezzange.domain.user.auth.repository.AuthToken;
import com.swyp.gaezzange.domain.user.auth.repository.AuthTokenRepository;
import com.swyp.gaezzange.util.TokenUserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil;
  private final AuthTokenRepository authTokenRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String requestUri = request.getRequestURI();
    if (requestUri.matches("^\\/login(?:\\/.*)?$") || requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {
      filterChain.doFilter(request, response);
      return;
    }

    if (requestUri.matches("^\\/api/v1/auth/token(?:\\/.*)?$")) {
      setTokenAfterLogin(request, response);
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = request.getHeader("Authorization");
    if (accessToken != null && accessToken.startsWith("Bearer ")) {
      accessToken = accessToken.substring(7);
      try {
        if (!jwtUtil.isExpired(accessToken)) {
          setSecurityContextByToken(accessToken);
        }
      } catch (JwtException e) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Token");
        return;
      }
    } else {
      Cookie[] cookies = request.getCookies();
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("refreshToken")) {
          accessToken = cookie.getValue();
          setSecurityContextByToken(accessToken);
        }
      }
    }

    filterChain.doFilter(request, response);
  }

  private void setSecurityContextByToken(String token) {
    Jws<Claims> claimsJws = jwtUtil.parseClaims(token);

    TokenUserContext context = TokenUserContext.builder()
        .userId(claimsJws.getBody().get("userId", Long.class))
        .userAuthId(claimsJws.getBody().get("userAuthId", Long.class))
        .email(claimsJws.getBody().get("email", String.class))
        .role(claimsJws.getBody().get("role", String.class))
        .provider(claimsJws.getBody().get("provider", String.class))
        .build();

    log.debug("user context: {}", context);

    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        context.getEmail(), context, jwtUtil.getAuthorities(context.getRole()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private void setTokenAfterLogin(HttpServletRequest request, HttpServletResponse response) {
    String tokenKey = request.getParameter("tokenKey");
    Optional<AuthToken> optionalAuthToken = authTokenRepository.findById(tokenKey);
    optionalAuthToken.filter(token -> token.getExpiresAt().isAfter(LocalDateTime.now()))
        .ifPresentOrElse(
            token -> response.setHeader("Authorization", "Bearer " + token.getToken()),
            () -> response.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
        );
  }
}
