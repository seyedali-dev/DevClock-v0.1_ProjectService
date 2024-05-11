package com.seyed.ali.projectservice.util.converter;

import com.seyed.ali.projectservice.util.KeycloakSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class KeycloakJwtAuthorityConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final KeycloakSecurityUtil keycloakSecurityUtil;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = this.keycloakSecurityUtil.extractAuthorities(jwt);
        return new JwtAuthenticationToken(jwt, authorities);
    }

}
