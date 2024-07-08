package com.swyp.gaezzange.api.user;

import com.swyp.gaezzange.api.user.dto.UserBasicInfoDto;
import com.swyp.gaezzange.util.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "UserController", description = "User API")
@RestController
public class UserController {

    @GetMapping("/user/my-info")
    public ApiResponse<UserBasicInfoDto> getUser() {
        return ApiResponse.success(new UserBasicInfoDto());
    }
}
