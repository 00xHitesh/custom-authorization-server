package com.authorization_server.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    //    private String authorityPrefix = "SCOPE_";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        String authoritiesClaimName = "authorities";
        if (!jwt.getClaims().containsKey(authoritiesClaimName)) return Collections.emptyList();

        if (jwt.getClaims().get(authoritiesClaimName) instanceof List) {
            Collection<String> authorities = (List<String>) jwt.getClaims().get(authoritiesClaimName);
            return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toCollection(HashSet::new));
        }
        return Collections.emptyList();
    }

}
