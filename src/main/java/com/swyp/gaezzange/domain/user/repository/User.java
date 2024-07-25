package com.swyp.gaezzange.domain.user.repository;

import static com.swyp.gaezzange.contants.SystemConstants.S3Constants.S3_URL;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.apache.logging.log4j.util.Strings.isBlank;

import com.swyp.gaezzange.api.user.dto.UserInfoForm;
import com.swyp.gaezzange.api.user.dto.UserProfileForm;
import com.swyp.gaezzange.domain.tendency.Tendency;
import com.swyp.gaezzange.util.jpa.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "users",
    indexes = {
        @Index(name = "users_idx_01", columnList = "createdAt"),
        @Index(name = "users_idx_02", columnList = "updatedAt"),
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "users_uk_01", columnNames = "nickname")
    }
)
public class User extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(nullable = false)
  private String nickname;

  @Column
  private String profileImagePath;

  @Setter
  @Enumerated(EnumType.STRING)
  private Tendency tendency;

  public static User from(UserInfoForm form) {
    return User.builder()
        .nickname(form.getNickname())
        .profileImagePath(form.getProfileImagePath())
        .tendency(form.getTendency())
        .build();
  }

  public User updateProfile(UserProfileForm form) {
    String profileImagePath = isBlank(form.getProfileImagePath()) ? this.profileImagePath : form.getProfileImagePath();

    return User.builder()
        .nickname(isBlank(form.getNickname()) ? this.nickname : form.getNickname())
        .profileImagePath(profileImagePath.replace(S3_URL, EMPTY))
        .build();
  }

  public String getProfileImagePathWithS3Url() {
    return profileImagePath == null ? null : S3_URL + profileImagePath;
  }
}
