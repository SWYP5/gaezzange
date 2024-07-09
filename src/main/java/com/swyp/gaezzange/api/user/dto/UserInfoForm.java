package com.swyp.gaezzange.api.user.dto;

import com.swyp.gaezzange.domain.character.UserCharacter;
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
    private UserCharacter character;
    private Boolean onboardingCompleted;
}
