package com.swyp.gaezzange.api.user.dto;

import com.swyp.gaezzange.domain.tendency.Tendency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicInfoDto {
    private String userId;
    private String nickname;
    private String profileImagePath;
    private Boolean onboardingCompleted;
    private Tendency character;
}
