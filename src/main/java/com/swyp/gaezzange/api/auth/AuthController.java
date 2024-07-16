package com.swyp.gaezzange.api.auth;

import com.swyp.gaezzange.domain.user.auth.repository.AuthToken;
import com.swyp.gaezzange.domain.user.auth.repository.AuthTokenRepository;
import com.swyp.gaezzange.jwt.JWTUtil;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "Auth", description = "Auth API")
@RestController("/v1/auth")
public class AuthController {

    @GetMapping("/token")
    public ApiResponse getToken(String email, HttpServletResponse response) {
        return ApiResponse.success(null);
    }
}
