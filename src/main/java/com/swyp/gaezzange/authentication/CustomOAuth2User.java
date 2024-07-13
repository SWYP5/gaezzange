package com.swyp.gaezzange.authentication;

import com.swyp.gaezzange.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final User user;

    public CustomOAuth2User(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add((GrantedAuthority) this.user::getRole);
        return collection;
    }

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        // 사용자의 추가 정보를 맵에 추가
        attributes.put("email", this.user.getEmail());
        // 다른 사용자 정보도 필요한 경우 여기에 추가
        return attributes;
    }

    @Override
    public String getName() {
        return this.user.getEmail();
    }

    public String getEmail() {
        return this.user != null ? this.user.getEmail() : null;
    }

    public String getProvider() {
        return this.user.getProvider();
    }
}
