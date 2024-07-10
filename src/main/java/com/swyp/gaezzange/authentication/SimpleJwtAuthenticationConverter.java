package com.swyp.gaezzange.authentication;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.Collection;


public abstract class SimpleJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

    @Override
    public final AbstractAuthenticationToken convert(Jwt jwt) {
        AbstractAuthenticationToken token = jwtAuthenticationConverter.convert(jwt);
        Collection<GrantedAuthority> authorities = token.getAuthorities();
        return convert(jwt, authorities);
    }

    public abstract AbstractAuthenticationToken convert(Jwt jwt, Collection<GrantedAuthority> authorities);
}
