package com.authorization_server.security;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.authorization_server.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
public class CustomOidcUser extends DefaultOidcUser implements UserDetails {
    private Integer id;
    private String username;
    private boolean active;
    private LocalDateTime createdAt;
    private Collection<? extends GrantedAuthority> authorities = new HashSet<>();

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken,
                          OidcUserInfo userInfo) {
        this(authorities, idToken, userInfo, IdTokenClaimNames.SUB);
    }

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities, OidcIdToken idToken,
                          OidcUserInfo userInfo, String nameAttributeKey) {
        super(AuthorityUtils.NO_AUTHORITIES, idToken, userInfo, nameAttributeKey);
        /**
         * Keep the authorities mutable
         */
        if (authorities != null) {
            this.authorities = authorities;
        }

        this.createdAt = LocalDateTime.now();
    }

    @JsonCreator
    public CustomOidcUser(@JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities,
                          @JsonProperty("idToken") OidcIdToken idToken,
                          @JsonProperty("userInfo") OidcUserInfo userInfo, @JsonProperty("id") Integer id,
                          @JsonProperty("username") String username, @JsonProperty("active") boolean active,
                          @JsonProperty("createdAt") LocalDateTime createdAt) {
        super(authorities, idToken, userInfo);
        this.id = id;
        this.username = username;
        this.active = active;
        this.createdAt = createdAt;
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public User toInstantUser() {

        //@formatter:off
        return User.builder()
                .userId(getId())
                .email(getEmail())
                .username(getUsername())
                .firstName(getGivenName())
                .middleName(getMiddleName())
                .lastName(getFamilyName())
                .federated(true)
                .avatarUrl(getPicture())
                .active(isActive())
                .build();
        //@formatter:on
    }
}