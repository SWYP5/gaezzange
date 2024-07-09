package com.swyp.gaezzange.api.user;

import com.swyp.gaezzange.api.user.dto.UserBasicInfoDto;
import com.swyp.gaezzange.api.user.dto.UserInfoUpdateForm;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserController", description = "User API")
@RestController
public class UserController {

    @GetMapping("/user/my-info")
    public ApiResponse<UserBasicInfoDto> getUser() {
        return ApiResponse.success(new UserBasicInfoDto());
    }

    @PutMapping("/user/my-info")
    public ApiResponse updateUserInfo(@RequestBody UserInfoUpdateForm form) {
        return ApiResponse.success(null);
    }

    @PostMapping("/user/onboarding")
    public ApiResponse completeOnboarding() {
        return ApiResponse.success(null);
    }
}

