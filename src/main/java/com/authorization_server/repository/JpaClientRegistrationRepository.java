package com.authorization_server.repository;

import com.authorization_server.entity.OidcClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.Set;

public class JpaClientRegistrationRepository implements ClientRegistrationRepository, Iterable<ClientRegistration> {

    private final OidcClientRepository oidcClientRepository;

    public JpaClientRegistrationRepository(OidcClientRepository oidcClientRepository) {
        this.oidcClientRepository = oidcClientRepository;
    }

    private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        }
        return new AuthorizationGrantType(authorizationGrantType);              // Custom authorization grant type
    }

    private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
        if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
        } else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.CLIENT_SECRET_POST;
        } else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
            return ClientAuthenticationMethod.NONE;
        }
        return new ClientAuthenticationMethod(clientAuthenticationMethod);      // Custom client authentication method
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        Assert.hasText(registrationId, "registrationId cannot be empty");
        return this.oidcClientRepository.findByRegistrationId(registrationId).map(this::toObject).orElse(null);
    }

    private ClientRegistration toObject(OidcClient oidcClient) {

        Set<String> clientScopes = StringUtils.commaDelimitedListToSet(oidcClient.scopes());

        //@formatter:off
        return ClientRegistration.withRegistrationId(oidcClient.registrationId())
                .clientId(oidcClient.clientId())
                .clientName(oidcClient.clientName())
                .clientSecret(oidcClient.clientSecret())
                .clientAuthenticationMethod(resolveClientAuthenticationMethod(oidcClient.clientAuthenticationMethods()))
                .authorizationGrantType(resolveAuthorizationGrantType(oidcClient.authorizationGrantType()))
                .redirectUri(oidcClient.redirectUri())
                .scope(clientScopes)
                .authorizationUri(oidcClient.authorizationUri())
                .tokenUri(oidcClient.tokenUri())
                .userInfoUri(oidcClient.userInfoUri())
                .userNameAttributeName(oidcClient.userNameAttributeName())
                .jwkSetUri(oidcClient.jwkSetUri())
                .build();
        //@formatter:on
    }

    @Override
    public Iterator<ClientRegistration> iterator() {
        return oidcClientRepository.findAll().stream().map(this::toObject).iterator();
    }
}
