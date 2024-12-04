package com.authorization_server.service;

import com.authorization_server.entity.User;
import com.authorization_server.security.mappers.OidcUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.security.oauth2.core.OAuth2Error;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserService userService;
    private final Map<String, OidcUserMapper> mappers;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Assert.isTrue(mappers.containsKey(registrationId), "No mapper defined for such registrationId");
        OidcUserMapper mapper = mappers.get(userRequest.getClientRegistration().getRegistrationId());
        String email = userRequest.getIdToken().getPreferredUsername();
        User localUser = userService.getByUsername(email);

        if (localUser != null) {
            return mapper.map(oidcUser.getIdToken(), oidcUser.getUserInfo(), localUser);
        }

        throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.ACCESS_DENIED), "ACCESS DENIED");
        //Map unregistered user
//        return mapper.map(oidcUser);
    }
}
