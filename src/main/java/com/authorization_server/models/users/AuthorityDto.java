package com.authorization_server.models.users;

import com.authorization_server.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDto {

    protected Integer authorityId;
    private String name;

//    public AuthorityDto() { }
    public static AuthorityDto fromEntity(Authority authority)
    {
        AuthorityDto authorityDto = new AuthorityDto();
        BeanUtils.copyProperties(authority, authorityDto);
        return authorityDto;
    }
}
