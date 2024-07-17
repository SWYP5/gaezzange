package com.swyp.gaezzange.domain.user.auth.repository;

import com.swyp.gaezzange.util.jpa.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "auth_tokens",
    indexes = {
        @Index(name = "auth_tokens_idx_01", columnList = "createdAt"),
        @Index(name = "auth_tokens_idx_02", columnList = "updatedAt"),
        @Index(name = "auth_tokens_idx_03", columnList = "userAuthId"),
    }
)
public class AuthToken extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String tokenId;

  @Column(nullable = false)
  private Long userAuthId;

  @Column(nullable = false)
  private String token;

  @Column(nullable = false)
  private LocalDateTime expiresAt;
}
