package com.swyp.gaezzange.domain.user.repository;

import com.swyp.gaezzange.util.jpa.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users",
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
}
