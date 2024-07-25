package com.swyp.gaezzange.api.user.dto;

import com.swyp.gaezzange.domain.tendency.Tendency;
import com.swyp.gaezzange.domain.user.repository.User;
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
  private Tendency tendency;

  public static UserBasicInfoDto from(User user) {
    return UserBasicInfoDto.builder()
        .userId(user.getUserId())
        .nickname(user.getNickname())
        .profileImagePath(user.getProfileImagePathWithS3Url())
        .tendency(user.getTendency())
        .build();
  }
}
