package com.authorization_server.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;

import java.util.Collection;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter;
    private final String principalClaimName;

    public CustomJwtAuthenticationConverter(Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter, String principalClaimName) {
        this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
        this.principalClaimName = principalClaimName != null ? principalClaimName : JwtClaimNames.SUB;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {

        Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(source);
        String principalClaimValue = source.getClaimAsString(this.principalClaimName);
        return new JwtUser(source, authorities, principalClaimValue);
    }
}
