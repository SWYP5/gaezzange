package com.swyp.gaezzange.api.auth;

import com.swyp.gaezzange.authentication.UserContextProvider;
import com.swyp.gaezzange.domain.user.UserApplication;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/auth")
public class AuthController {

    private final UserApplication userApplication;
    private final UserContextProvider userContextProvider;

    @GetMapping("/token")
    public ApiResponse getToken() {

        // 로그인 시 업데이트
        userContextProvider.getUserIdOptional()
            .ifPresent(userId -> userApplication.syncUserTendency(userId));

        return ApiResponse.success(null);
    }
}
