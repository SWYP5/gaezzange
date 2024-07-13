package com.swyp.gaezzange.authentication;

import com.swyp.gaezzange.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        List<String> roleNames = new ArrayList<>();
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
        customUserDetails.getAuthorities().forEach(authority->{
            roleNames.add(authority.getAuthority());
        });
        String email = authentication.getName();
        String provider = customUserDetails.getProvider();
        String role = roleNames.get(0);
        // AccessToken 생성
        String accessToken = jwtUtil.createJwt("access", email, role, provider, 3000000);

        // RefreshToken 생성 및 Redis에 저장
        String refreshToken = jwtUtil.createJwt("refresh", email, role, provider, 3000000);

        // RefreshToken 쿠키에 설정
        response.addCookie(createCookie("refreshToken", refreshToken, (int) (3000000 / 1000)));

        // AccessToken 헤더에 설정
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect("http://localhost:8080/api/hello");
    }

    private Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        return cookie;
    }


}
