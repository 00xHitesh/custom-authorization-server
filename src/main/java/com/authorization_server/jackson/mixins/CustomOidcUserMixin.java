package com.authorization_server.jackson.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;

public interface CustomOidcUserMixin {
    @JsonProperty("id")
    Integer getId();
    @JsonProperty("username")
    String getUsername();
    @JsonProperty("active")
    boolean getActive();
    @JsonProperty("createdAt")
    LocalDateTime getCreatedAt();
    @JsonProperty("authorities")
    Collection<? extends GrantedAuthority> getAuthorities();
}
