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
public class UserInfoForm {
    private String nickname;
    private String profileImagePath;
    private Tendency character;
    private Boolean onboardingCompleted;
}
