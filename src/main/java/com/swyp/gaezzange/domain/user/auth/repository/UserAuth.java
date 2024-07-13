package com.swyp.gaezzange.domain.user.auth.repository;

import com.swyp.gaezzange.domain.user.repository.User;
import com.swyp.gaezzange.domain.user.role.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_auths")
public class UserAuth {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userAuthId;

  @OneToOne(fetch =  FetchType.EAGER)
  @JoinColumn(name = "userId", referencedColumnName = "userId")
  private User user;

  @Column(unique = true, nullable = false)
  private String email;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  @Column(nullable = false)
  private String provider;

  @Column(nullable = false)
  private String providerCode;

  public static UserAuth createUserAuth(String email, String provider, UserRole role, String providerCode) {
    return UserAuth.builder()
        .email(email)
        .role(role)
        .provider(provider)
        .providerCode(providerCode)
        .build();
  }
}
