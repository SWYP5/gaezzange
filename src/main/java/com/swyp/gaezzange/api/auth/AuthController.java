package com.swyp.gaezzange.api.auth;

import com.swyp.gaezzange.domain.user.auth.repository.AuthTokenRepository;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth API")
@RestController("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthTokenRepository authTokenRepository;


    @GetMapping("/token")
    public ApiResponse getToken(String tokenKey, HttpServletResponse response) {
//        Optional<AuthToken> optionalAuthToken = authTokenRepository.findById(tokenKey);
//        optionalAuthToken.filter(token -> token.getExpiresAt().before(new Date()))
//            .ifPresentOrElse(
//                token -> response.setHeader("Authorization", "Bearer " + token.getToken()),
//                ()-> { throw new RuntimeException("만료"); }
//            );
        return ApiResponse.success(null);
    }
}
