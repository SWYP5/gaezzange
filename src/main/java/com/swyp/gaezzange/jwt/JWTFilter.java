package com.swyp.gaezzange.jwt;

import com.swyp.gaezzange.domain.user.auth.repository.AuthToken;
import com.swyp.gaezzange.domain.user.auth.repository.AuthTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final AuthTokenRepository authTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^\\/api/token(?:\\/.*)?$")) {
            String tokenKey = request.getParameter("tokenKey");
            Optional<AuthToken> optionalAuthToken = authTokenRepository.findById(tokenKey);
            optionalAuthToken.filter(token -> token.getExpiresAt().before(new Date()))
                .ifPresentOrElse(
                    token -> response.setHeader("Authorization", "Bearer " + token.getToken()),
                    ()-> { throw new RuntimeException("만료"); }
                );

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
                e.printStackTrace();
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
        String email = claimsJws.getBody().get("email", String.class);
        String role = claimsJws.getBody().get("role", String.class);
        String provider = claimsJws.getBody().get("provider", String.class);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, null, jwtUtil.getAuthorities(role));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
