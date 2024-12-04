package com.authorization_server.security.mappers;

import com.authorization_server.entity.User;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public interface OidcUserMapper {
    /**
     * Map unregistered user
     * @param oidcUser
     * @return
     */
    OidcUser map(OidcUser oidcUser);
    OidcUser map(OidcIdToken idToken, OidcUserInfo userInfo, User user);
}
