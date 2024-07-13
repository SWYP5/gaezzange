package com.swyp.gaezzange.domain;

import com.swyp.gaezzange.domain.user.role.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    private String userId;

    @NonNull
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "img_path")
    private String imgPath;

    @Column(name = "nickname", unique = true)
    private String nickname;

    private UserRole role;
    private String provider;
    private String providerCode;


    public static User createAuthUser(String email, String provider, UserRole role, String providerCode) {
        return User.builder()
                .email(email)
                .provider(provider)
                .role(role)
                .providerCode(providerCode)
                .build();
    }

}
