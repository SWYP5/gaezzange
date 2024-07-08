package com.swyp.gaezzange.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicInfoDto {
    private Long userId;
    private String nickname;
    private String profileImagePath;
    private Boolean onboardingCompleted;
    private String character;
}
