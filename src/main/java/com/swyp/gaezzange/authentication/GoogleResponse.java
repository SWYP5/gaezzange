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
        // Using Optional to safely handle null values and ensuring a non-null String is returned.
        return Optional.ofNullable(attribute.get("sub"))
                .map(Object::toString)
                .orElse(""); // Returning empty String instead of null.
    }

    @Override
    public String getEmail() {
        return Optional.ofNullable(attribute.get("email"))
                .map(Object::toString)
                .orElse(""); // Consistently handling Optional to return non-null String.
    }
}
