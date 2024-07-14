package com.swyp.gaezzange.domain.user.auth.repository;

import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.domain.user.role.UserRole;
import com.swyp.gaezzange.util.jpa.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_auths",
    indexes = {
        @Index(name = "user_auths_idx_01", columnList = "createdAt"),
        @Index(name = "user_auths_idx_02", columnList = "updatedAt"),
        @Index(name = "user_auths_idx_03", columnList = "email"),
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "user_auths_uk_01", columnNames = "userId"),
        @UniqueConstraint(name = "user_auths_uk_02", columnNames = "providerCode")
    }
)
public class UserAuth extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userAuthId;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "userId", referencedColumnName = "userId", nullable = true)
  private User user;

  @Column(nullable = false)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  @Column(nullable = false)
  private String provider;

  @Column(nullable = false)
  private String providerCode;

  public static UserAuth createUserAuth(String email, String provider, UserRole role,
      String providerCode) {
    return UserAuth.builder()
        .email(email)
        .role(role)
        .provider(provider)
        .providerCode(providerCode)
        .build();
  }
}
