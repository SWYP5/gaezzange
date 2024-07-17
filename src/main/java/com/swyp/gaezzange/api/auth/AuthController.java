package com.swyp.gaezzange.api.auth;

import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "Auth API")
@RestController
public class AuthController {
    @GetMapping("/v1/auth/token")
    public ApiResponse getToken() {
        return ApiResponse.success(null);
    }
}
