package com.authorization_server.models.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.authorization_server.entity.Role;
import com.authorization_server.entity.User;
import com.authorization_server.repository.RoleRepository;
import lombok.*;

import java.util.HashSet;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer userId;

    private String username;
    @JsonInclude
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String locale;
    private String avatarUrl;
    private Boolean active;
    private Boolean federated;
    private String email;
    private Boolean emailVerified;
    private List<Integer> roles;

    public static UserDto fromEntity(User user) {
        //@formatter:off
        return UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .emailVerified(user.getEmailVerified() == 1)
                .avatarUrl(user.getAvatarUrl())
                .locale(user.getLocale())
                .federated(user.getFederated())
                .active(user.getActive())
                .username(user.getUsername())
                .roles(user.getRoles().stream().map(Role::getRoleId).toList())
                .build();
        //@formatter:on
    }

    public User toEntity(RoleRepository roleRepository) {
        //@formatter:off
        return User.builder()
                .username(username)
                .firstName(firstName)
                .middleName(middleName)
                .lastName(lastName)
                .avatarUrl(avatarUrl)
                .locale(locale)
                .active(active)
                .federated(federated)
                .email(email)
                .emailVerified(emailVerified)
                .password(password)
                .roles(new HashSet<>(roleRepository.findByRoleIdIn(roles)))
            .build();
        //@formatter:off
    }
}
