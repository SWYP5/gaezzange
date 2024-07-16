package com.swyp.gaezzange.api.auth;

import com.swyp.gaezzange.domain.user.auth.repository.AuthToken;
import com.swyp.gaezzange.domain.user.auth.repository.AuthTokenRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "Auth", description = "Auth API")
@RequiredArgsConstructor
@RestController("/v1/auth")
public class AuthController {

    private final AuthTokenRepository authTokenRepository;

    @GetMapping("/token")
    public String getToken(String email) {
        Optional<AuthToken> authToken = authTokenRepository.findAuthTokenByEmail(email);
        return authToken.map(AuthToken::getToken).orElse(null);
    }
}
