package com.authorization_server.entity;

import jakarta.persistence.*;
import java.util.List;
@Entity
@Table(name = "`oidc_client`", schema = "auth_server")
public class OidcClient {
    @Id
    private String registrationId;
    @Column(length = 200)
    private String clientId;
    @Column(length = 200)
    private String clientName;
    @Column(length = 500)
    private String clientSecret;
    @Column(length = 500)
    private String clientAuthenticationMethods;
    @Column(length = 200)
    private String authorizationGrantType;
    @Column(length = 500)
    private String redirectUri;
    @Column(length = 500)
    private String scopes;
    @Column(length = 500)
    private String authorizationUri;
    @Column(length = 500)
    private String tokenUri;
    @Column(length = 200)
    private String userInfoUri;
    @Column(length = 100)
    private String userNameAttributeName;
    @Column(length = 500)
    private String jwkSetUri;
    @Column(length = 200)
    private String userInfoAuthenticationMethod;
    @Column(length = 65535)
    private String providerConfigurationMetadata;
    @Column(length = 500)
    private String issuerUri;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "oidc_registration_id")
    private List<SsoDomain> ssoDomains;

    public String registrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String clientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String clientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String clientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String clientAuthenticationMethods() {
        return clientAuthenticationMethods;
    }

    public void setClientAuthenticationMethods(String clientAuthenticationMethods) {
        this.clientAuthenticationMethods = clientAuthenticationMethods;
    }

    public String authorizationGrantType() {
        return authorizationGrantType;
    }

    public void setAuthorizationGrantType(String authorizationGrantType) {
        this.authorizationGrantType = authorizationGrantType;
    }

    public String redirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String scopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

    public String authorizationUri() {
        return authorizationUri;
    }

    public void setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
    }

    public String tokenUri() {
        return tokenUri;
    }

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    public String userInfoUri() {
        return userInfoUri;
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }

    public String userNameAttributeName() {
        return userNameAttributeName;
    }

    public void setUserNameAttributeName(String userNameAttributeName) {
        this.userNameAttributeName = userNameAttributeName;
    }

    public String jwkSetUri() {
        return jwkSetUri;
    }

    public void setJwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }

    public String userInfoAuthenticationMethod() {
        return userInfoAuthenticationMethod;
    }

    public void setUserInfoAuthenticationMethod(String userInfoAuthenticationMethod) {
        this.userInfoAuthenticationMethod = userInfoAuthenticationMethod;
    }

    public String providerConfigurationMetadata() {
        return providerConfigurationMetadata;
    }

    public void setProviderConfigurationMetadata(String providerConfigurationMetadata) {
        this.providerConfigurationMetadata = providerConfigurationMetadata;
    }

    public String issuerUri() {
        return issuerUri;
    }

    public void setIssuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
    }
}
