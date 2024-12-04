package com.authorization_server.security;

import com.authorization_server.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
public class CustomUserDetails implements UserDetails {
    private String password;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private String firstName;
    private String lastName;

    public CustomUserDetails(){
    }
    public CustomUserDetails(String username, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public CustomUserDetails(String username, String password, Collection<String> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities.stream().map(SimpleGrantedAuthority::new).toList();
    }

    public CustomUserDetails(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        List<SimpleGrantedAuthority> immutableList = user.getRoles().stream()
                .flatMap(role -> role.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                ).toList();
        this.authorities = new ArrayList<>(immutableList);
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(password, that.password) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password, username);
    }
}
