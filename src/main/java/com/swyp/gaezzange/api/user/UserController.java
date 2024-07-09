package com.swyp.gaezzange.api.user;

import com.swyp.gaezzange.api.user.dto.UserBasicInfoDto;
import com.swyp.gaezzange.api.user.dto.UserInfoForm;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserController", description = "User API")
@RestController("/v1/user")
public class UserController {

    @GetMapping("/my-info")
    public ApiResponse<UserBasicInfoDto> getUser() {
        return ApiResponse.success(new UserBasicInfoDto());
    }

    @PostMapping("/my-info")
    public ApiResponse updateUserInfo(@RequestBody UserInfoForm form) {
        return ApiResponse.success(null);
    }
}

