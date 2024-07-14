package com.swyp.gaezzange.authentication;

import com.swyp.gaezzange.domain.user.auth.repository.UserAuth;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    private final UserAuth userAuth;

    public CustomOAuth2User(UserAuth userAuth) {
        this.userAuth = userAuth;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userAuth.getRole().name()));
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        // 사용자의 추가 정보를 맵에 추가
        attributes.put("email", this.userAuth.getEmail());
        // 다른 사용자 정보도 필요한 경우 여기에 추가
        return attributes;
    }

    @Override
    public String getName() {
        return this.userAuth.getEmail();
    }

    public Long getUserAuthId() {
        return this.userAuth.getUserAuthId();
    }

    public Long getUserId() {
        return Optional.ofNullable(this.userAuth.getUser()).map(user -> user.getUserId()).orElse(null);
    }

    public String getEmail() {
        return this.userAuth != null ? this.userAuth.getEmail() : null;
    }

    public String getProvider() {
        return this.userAuth.getProvider();
    }
}
