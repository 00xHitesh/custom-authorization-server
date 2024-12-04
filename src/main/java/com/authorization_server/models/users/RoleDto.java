package com.authorization_server.models.users;

import com.authorization_server.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.HashSet;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RoleDto {
    private Integer roleId;
    private Set<AuthorityDto> authorities = new HashSet<>();
    private String name;

//    public RoleDto() { }

    public static RoleDto fromEntity(Role role)
    {
        RoleDto roleDto = new RoleDto();
        BeanUtils.copyProperties(role, roleDto);
        return roleDto;
    }
}

