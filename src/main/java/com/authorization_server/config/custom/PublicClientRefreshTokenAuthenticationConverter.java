package com.authorization_server.config.custom;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

public class PublicClientRefreshTokenAuthenticationConverter implements AuthenticationConverter {

    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(grantType)) {
            return null;
        }

        // client_id (REQUIRED)
        String clientId = request.getParameter(OAuth2ParameterNames.CLIENT_ID);
        if (!StringUtils.hasText(clientId)) {
            return null;
        }
        // client_secret (NONE)
        String clientSecret = request.getParameter(OAuth2ParameterNames.CLIENT_SECRET);
        if (clientSecret == null || StringUtils.hasText(clientSecret)) {
            return null;
        }

        return new PublicClientRefreshTokenAuthenticationToken(clientId);
    }
}
