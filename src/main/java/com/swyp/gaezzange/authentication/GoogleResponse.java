package com.swyp.gaezzange.authentication;

import com.swyp.gaezzange.util.OAuth2Response;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class GoogleResponse implements OAuth2Response {

    private final Map<String, Object> attribute;

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return Optional.ofNullable(attribute.get("sub"))
                .map(Object::toString)
                .orElse("");
    }

    @Override
    public String getEmail() {
        return Optional.ofNullable(attribute.get("email"))
                .map(Object::toString)
                .orElse("");
    }
}
