package com.authorization_server.jackson.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public interface CustomUserDetailsMixin {
    @JsonProperty("username")
    String getUsername();

    @JsonProperty("authorities")
    Collection<? extends GrantedAuthority> getAuthorities();

    @JsonIgnore
    boolean isAccountNonExpired();

    @JsonIgnore
    boolean isAccountNonLocked();

    @JsonIgnore
    boolean isCredentialsNonExpired();

    @JsonIgnore
    boolean isEnabled();
}
