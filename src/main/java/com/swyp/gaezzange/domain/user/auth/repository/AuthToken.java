package com.swyp.gaezzange.domain.user.auth.repository;

import com.swyp.gaezzange.util.jpa.BaseTimeEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
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
        name = "tokens"
)
public class AuthToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String tokenId;

    private String email;

    private String token;

    private Date expiresAt;
}
